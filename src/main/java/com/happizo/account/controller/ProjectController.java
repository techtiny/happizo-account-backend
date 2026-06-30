package com.happizo.account.controller;

import com.happizo.account.dto.ProjectDto;
import com.happizo.account.entity.Project;
import com.happizo.account.repository.ExpenseItemRepository;
import com.happizo.account.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExpenseItemRepository expenseItemRepository;
    private final ProjectRepository projectRepository;

    @Value("${pwj.api.url:http://localhost:8081}")
    private String pwjApiUrl;

    private static final List<String> CATEGORIES =
            List.of("MATERIAL", "SUBCONTRACT", "CONSULTANTS", "LABOUR", "MISCELLANEOUS");

    public ProjectController(ExpenseItemRepository expenseItemRepository, ProjectRepository projectRepository) {
        this.expenseItemRepository = expenseItemRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<ProjectDto>> getAll() {
        // 1. expense_items totals: projectId -> category -> sum
        Map<Long, Map<String, BigDecimal>> expenseMap = new HashMap<>();
        for (Object[] row : expenseItemRepository.getSummaryAllProjects()) {
            Long projId = ((Number) row[0]).longValue();
            String cat = (String) row[1];
            BigDecimal total = dec(row[2]);
            expenseMap.computeIfAbsent(projId, k -> new HashMap<>()).put(cat, total);
        }

        // 2. PWJ docs totals: projectName(lower) -> pwjType -> sum gross
        Map<String, Map<String, BigDecimal>> pwjDocsMap = buildPwjDocsMap();

        try {
            Map<String, Object> resp = restTemplate.getForObject(
                    pwjApiUrl + "/api/v1/projects/active", Map.class);
            List<Map<String, Object>> data = resp != null
                    ? (List<Map<String, Object>>) resp.get("data")
                    : List.of();
            List<ProjectDto> dtos = (data != null ? data : List.<Map<String, Object>>of())
                    .stream().map(m -> toDtoWithExpenses(m, expenseMap, pwjDocsMap)).collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, BigDecimal>> buildPwjDocsMap() {
        Map<String, Map<String, BigDecimal>> result = new HashMap<>();
        try {
            Map<String, Object> resp = restTemplate.getForObject(
                    pwjApiUrl + "/api/v1/pwj/entries/docs", Map.class);
            Object dataObj = resp != null ? resp.get("data") : null;
            List<Map<String, Object>> docs = dataObj instanceof List
                    ? (List<Map<String, Object>>) dataObj : List.of();
            for (Map<String, Object> doc : docs) {
                String pName = doc.get("projectName") instanceof String
                        ? ((String) doc.get("projectName")).trim().toLowerCase() : null;
                String pType = doc.get("pwjType") instanceof String
                        ? (String) doc.get("pwjType") : null;
                if (pName == null || pType == null) continue;
                BigDecimal gross = doc.get("gross") instanceof Number
                        ? new BigDecimal(doc.get("gross").toString()) : BigDecimal.ZERO;
                result.computeIfAbsent(pName, k -> new HashMap<>())
                      .merge(pType.toUpperCase(), gross, BigDecimal::add);
            }
        } catch (Exception ignored) {}
        return result;
    }

    private ProjectDto toDtoWithExpenses(Map<String, Object> m,
            Map<Long, Map<String, BigDecimal>> expenseMap,
            Map<String, Map<String, BigDecimal>> pwjDocsMap) {
        ProjectDto dto = toDto(m);
        if (dto.getId() != null) {
            // Saved expense_items (manual/confirmed entries)
            Map<String, BigDecimal> cats = expenseMap.getOrDefault(dto.getId(), Map.of());

            // PWJ docs by project name (auto-populated source)
            String nameKey = dto.getName() != null ? dto.getName().trim().toLowerCase() : "";
            Map<String, BigDecimal> pwjTypes = pwjDocsMap.getOrDefault(nameKey, Map.of());

            // Use expense_items value if non-zero, else fall back to PWJ docs total
            BigDecimal material = cats.getOrDefault("MATERIAL", BigDecimal.ZERO);
            BigDecimal labour   = cats.getOrDefault("LABOUR",   BigDecimal.ZERO);
            BigDecimal subcon   = cats.getOrDefault("SUBCONTRACT",   BigDecimal.ZERO);
            BigDecimal consult  = cats.getOrDefault("CONSULTANTS",   BigDecimal.ZERO);
            BigDecimal misc     = cats.getOrDefault("MISCELLANEOUS", BigDecimal.ZERO);

            if (material.compareTo(BigDecimal.ZERO) == 0)
                material = pwjTypes.getOrDefault("PO", BigDecimal.ZERO);
            if (subcon.compareTo(BigDecimal.ZERO) == 0)
                subcon = pwjTypes.getOrDefault("WO", BigDecimal.ZERO);
            if (labour.compareTo(BigDecimal.ZERO) == 0)
                labour = pwjTypes.getOrDefault("JO", BigDecimal.ZERO);

            dto.setExpenses(new ProjectDto.Expenses(material, labour, subcon, consult, misc));
        }
        return dto;
    }

    @SuppressWarnings("unchecked")
    private BigDecimal fetchPwjDocGst(String projectName) {
        if (projectName == null) return BigDecimal.ZERO;
        try {
            Map<String, Object> resp = restTemplate.getForObject(
                    pwjApiUrl + "/api/v1/pwj/entries/docs", Map.class);
            Object dataObj = resp != null ? resp.get("data") : null;
            List<Map<String, Object>> docs = dataObj instanceof List
                    ? (List<Map<String, Object>>) dataObj : List.of();
            BigDecimal total = BigDecimal.ZERO;
            for (Map<String, Object> doc : docs) {
                String pName = doc.get("projectName") instanceof String
                        ? ((String) doc.get("projectName")).trim() : null;
                if (pName == null || !pName.equalsIgnoreCase(projectName.trim())) continue;
                BigDecimal gst = doc.get("gstAmount") instanceof Number
                        ? new BigDecimal(doc.get("gstAmount").toString()) : BigDecimal.ZERO;
                total = total.add(gst);
            }
            return total;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getById(@PathVariable Long id) {
        Map<String, Object> m = fetchPwjProject(id);
        if (m == null) throw new IllegalArgumentException("Project not found: " + id);
        return ResponseEntity.ok(toDto(m));
    }

    @GetMapping("/{id}/expense-summary")
    public ResponseEntity<Map<String, Object>> getExpenseSummary(@PathVariable Long id) {
        Map<String, Object> proj = fetchPwjProject(id);
        String projectName = proj != null ? (String) proj.get("name") : "Unknown";
        String clientName  = proj != null ? (String) proj.get("clientName") : null;
        BigDecimal quoteGross = proj != null && proj.get("quoteValue") instanceof Number
                ? new BigDecimal(proj.get("quoteValue").toString()) : BigDecimal.ZERO;

        Map<String, BigDecimal[]> db = new HashMap<>();
        for (Object[] row : expenseItemRepository.getSummaryByProject(id)) {
            String cat    = (String) row[0];
            BigDecimal pwj    = dec(row[1]);
            BigDecimal paid   = dec(row[2]);
            BigDecimal vendor = dec(row[3]);
            db.put(cat, new BigDecimal[]{pwj, paid, vendor});
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        BigDecimal totPwj = BigDecimal.ZERO, totPaid = BigDecimal.ZERO, totVendor = BigDecimal.ZERO;
        for (String cat : CATEGORIES) {
            BigDecimal[] vals = db.getOrDefault(cat, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO});
            BigDecimal pwj = vals[0], paid = vals[1], vendor = vals[2];
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("category",      cat);
            row.put("pwjTotal",      pwj);
            row.put("actualPaid",    paid);
            row.put("vendorTotal",   vendor);
            row.put("balancePwj",    pwj.subtract(paid));
            row.put("balanceActual", vendor.subtract(paid));
            rows.add(row);
            totPwj    = totPwj.add(pwj);
            totPaid   = totPaid.add(paid);
            totVendor = totVendor.add(vendor);
        }

        BigDecimal balancePwj    = totPwj.subtract(totPaid);
        BigDecimal balanceActual = totVendor.subtract(totPaid);

        Map<String, Object> totals = new LinkedHashMap<>();
        totals.put("pwjTotal",     totPwj);
        totals.put("actualPaid",   totPaid);
        totals.put("vendorTotal",  totVendor);
        totals.put("balancePwj",   balancePwj);
        totals.put("balanceActual",balanceActual);

        // Total Expenses for the Project = Total Paid Expenses + Total Balance to be Paid (per column)
        BigDecimal totalExpensesPwj    = totPwj.add(balancePwj);
        BigDecimal totalExpensesActual = totPaid.add(balanceActual);

        // GST: As per PO is not tracked at the document level — left blank.
        // Actual GST uses the same formula as the Project-wise "Total GST" card:
        // PWJ-doc GST (PO/WO/JO gstAmount) + vendor GST (expense_items.vendorGstAmount)
        BigDecimal gstPwj = null;
        BigDecimal vendorGst = BigDecimal.ZERO;
        List<Object[]> gstRows = expenseItemRepository.getGstTotalsByProject(id);
        if (!gstRows.isEmpty()) {
            vendorGst = dec(gstRows.get(0)[1]);
        }
        BigDecimal pwjDocGst = fetchPwjDocGst(projectName);
        BigDecimal gstActual = pwjDocGst.add(vendorGst);

        // Gross expenses for P&L = total expenses (actual), net of GST (GST is pass-through, not a real cost)
        BigDecimal grossExpensesActual = totalExpensesActual.subtract(gstActual);

        // Collection received is tracked on the local Project record, matched by name
        BigDecimal collectionReceived = projectName != null
                ? projectRepository.findByNameIgnoreCase(projectName)
                        .map(Project::getCollectionReceived)
                        .map(v -> v == null ? BigDecimal.ZERO : v)
                        .orElse(BigDecimal.ZERO)
                : BigDecimal.ZERO;
        // Balance Due (top stat) = Collection Received - Total Paid (Actual)
        BigDecimal balanceDue = collectionReceived.subtract(totPaid);
        // Balance as on Date (bottom banner) = Collection Received - Total Balance to be Paid (Actual)
        BigDecimal balanceAsOnDate = collectionReceived.subtract(balanceActual);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("projectName",          projectName);
        result.put("client",               clientName);
        result.put("fy",                   null);
        result.put("quoteGross",           quoteGross);
        result.put("collectionReceived",   collectionReceived);
        result.put("categories",           rows);
        result.put("totals",               totals);
        result.put("totalExpensesPwj",     totalExpensesPwj);
        result.put("totalExpensesActual",  totalExpensesActual);
        result.put("gstPwj",               gstPwj);
        result.put("gstActual",            gstActual);
        result.put("grossExpensesActual",  grossExpensesActual);
        result.put("balanceDue",           balanceDue);
        result.put("balanceAsOnDate",      balanceAsOnDate);
        return ResponseEntity.ok(result);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> fetchPwjProject(Long id) {
        try {
            Map<String, Object> resp = restTemplate.getForObject(
                    pwjApiUrl + "/api/v1/projects/active", Map.class);
            List<Map<String, Object>> data = resp != null
                    ? (List<Map<String, Object>>) resp.get("data") : null;
            if (data == null) return null;
            return data.stream()
                    .filter(p -> p.get("id") instanceof Number
                            && id == ((Number) p.get("id")).longValue())
                    .findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private ProjectDto toDto(Map<String, Object> m) {
        ProjectDto dto = new ProjectDto();
        dto.setId(m.get("id") instanceof Number ? ((Number) m.get("id")).longValue() : null);
        dto.setName((String) m.get("name"));
        dto.setClient((String) m.get("clientName"));
        dto.setLocation((String) m.get("location"));
        dto.setDescription((String) m.get("description"));
        dto.setClientGstNo((String) m.get("clientGstNo"));
        dto.setClientAddress((String) m.get("clientAddress"));
        dto.setBillingAddress((String) m.get("billingAddress"));
        Object active = m.get("active");
        dto.setStatus(Boolean.TRUE.equals(active) ? "active" : "inactive");
        if (m.get("quoteValue") instanceof Number)
            dto.setQuoteGross(new BigDecimal(m.get("quoteValue").toString()));
        if (m.get("totalValue") instanceof Number)
            dto.setTotalValue(new BigDecimal(m.get("totalValue").toString()));
        return dto;
    }

    private BigDecimal dec(Object o) {
        if (o == null) return BigDecimal.ZERO;
        if (o instanceof BigDecimal) return (BigDecimal) o;
        return new BigDecimal(o.toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}
