package com.happizo.account.service;

import com.happizo.account.dto.ProjectDto;
import com.happizo.account.entity.Project;
import com.happizo.account.repository.ExpenseItemRepository;
import com.happizo.account.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final BigDecimal BUDGET_RATIO = new BigDecimal("0.80");

    private static final List<String> CATEGORIES =
            List.of("MATERIAL", "SUBCONTRACT", "CONSULTANTS", "LABOUR", "MISCELLANEOUS");

    private final ProjectRepository projectRepository;
    private final ExpenseItemRepository expenseItemRepository;

    public ProjectService(ProjectRepository projectRepository, ExpenseItemRepository expenseItemRepository) {
        this.projectRepository = projectRepository;
        this.expenseItemRepository = expenseItemRepository;
    }

    public List<ProjectDto> getAll() {
        return projectRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProjectDto getById(Long id) {
        return projectRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }

    public ProjectDto create(ProjectDto dto) {
        Project project = new Project();
        applyDto(project, dto);
        project.setId(null);
        validateBudget(project);
        return toDto(projectRepository.save(project));
    }

    public ProjectDto update(Long id, ProjectDto dto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
        applyDto(existing, dto);
        validateBudget(existing);
        return toDto(projectRepository.save(existing));
    }

    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found: " + id);
        }
        projectRepository.deleteById(id);
    }

    // ── Expense summary for the PDF summary view ─────────────────────────────
    public Map<String, Object> getExpenseSummary(Long projectId) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

        // Build per-category map from DB
        Map<String, BigDecimal[]> db = new HashMap<>();
        for (Object[] row : expenseItemRepository.getSummaryByProject(projectId)) {
            String cat    = (String) row[0];
            BigDecimal pwj    = dec(row[1]);
            BigDecimal paid   = dec(row[2]);
            BigDecimal vendor = dec(row[3]);
            db.put(cat, new BigDecimal[]{pwj, paid, vendor});
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        BigDecimal totPwj = BigDecimal.ZERO, totPaid = BigDecimal.ZERO,
                   totVendor = BigDecimal.ZERO;

        for (String cat : CATEGORIES) {
            BigDecimal[] vals = db.getOrDefault(cat, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO});
            BigDecimal pwj    = vals[0];
            BigDecimal paid   = vals[1];
            BigDecimal vendor = vals[2];
            BigDecimal balPwj    = pwj.subtract(paid);
            BigDecimal balActual = vendor.subtract(paid);

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("category",     cat);
            row.put("pwjTotal",     pwj);     // As per PO paid total
            row.put("actualPaid",   paid);    // Actual paid
            row.put("vendorTotal",  vendor);
            row.put("balancePwj",   balPwj);
            row.put("balanceActual",balActual);
            rows.add(row);

            totPwj    = totPwj.add(pwj);
            totPaid   = totPaid.add(paid);
            totVendor = totVendor.add(vendor);
        }

        Map<String, Object> totals = new LinkedHashMap<>();
        totals.put("pwjTotal",     totPwj);
        totals.put("actualPaid",   totPaid);
        totals.put("vendorTotal",  totVendor);
        totals.put("balancePwj",   totPwj.subtract(totPaid));
        totals.put("balanceActual",totVendor.subtract(totPaid));

        // Balance as on date = collectionReceived - totalPaid
        BigDecimal balanceOnDate = safe(p.getCollectionReceived()).subtract(totPaid);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("projectName",        p.getName());
        result.put("client",             p.getClient());
        result.put("fy",                 p.getFy());
        result.put("quoteGross",         safe(p.getQuoteGross()));
        result.put("collectionReceived", safe(p.getCollectionReceived()));
        result.put("categories",         rows);
        result.put("totals",             totals);
        result.put("balanceAsOnDate",    balanceOnDate);
        return result;
    }

    private BigDecimal dec(Object o) {
        if (o == null) return BigDecimal.ZERO;
        if (o instanceof BigDecimal) return (BigDecimal) o;
        return new BigDecimal(o.toString());
    }

    private void validateBudget(Project p) {
        BigDecimal budget = safe(p.getQuoteGross()).multiply(BUDGET_RATIO);
        BigDecimal spent = totalExpenses(p);
        if (budget.compareTo(BigDecimal.ZERO) > 0 && spent.compareTo(budget) > 0) {
            throw new IllegalStateException(
                String.format("Total expenses ₹%.0f exceed the 80%% budget limit of ₹%.0f", spent, budget));
        }
    }

    private BigDecimal totalExpenses(Project p) {
        return safe(p.getExpMaterial())
                .add(safe(p.getExpLabour()))
                .add(safe(p.getExpSubcontract()))
                .add(safe(p.getExpConsultants()))
                .add(safe(p.getExpMiscellaneous()));
    }

    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private ProjectDto toDto(Project p) {
        ProjectDto dto = new ProjectDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setClient(p.getClient());
        dto.setFy(p.getFy());
        dto.setStatus(p.getStatus());
        dto.setBusinessType(p.getBusinessType());
        dto.setLocation(p.getLocation());
        dto.setDescription(p.getDescription());
        dto.setClientGstNo(p.getClientGstNo());
        dto.setClientAddress(p.getClientAddress());
        dto.setBillingAddress(p.getBillingAddress());
        dto.setPoWoStatus(p.getPoWoStatus());
        dto.setGstPct(p.getGstPct());
        dto.setQuoteGstPct(p.getQuoteGstPct());
        dto.setQuoteGross(safe(p.getQuoteGross()));
        dto.setQuoteValue(p.getQuoteValue());
        dto.setTotalValue(p.getTotalValue());
        dto.setCollectionReceived(safe(p.getCollectionReceived()));
        dto.setExpenses(new ProjectDto.Expenses(
                safe(p.getExpMaterial()),
                safe(p.getExpLabour()),
                safe(p.getExpSubcontract()),
                safe(p.getExpConsultants()),
                safe(p.getExpMiscellaneous())
        ));
        return dto;
    }

    private void applyDto(Project p, ProjectDto dto) {
        if (dto.getName() != null)               p.setName(dto.getName());
        if (dto.getClient() != null)             p.setClient(dto.getClient());
        if (dto.getFy() != null)                 p.setFy(dto.getFy());
        if (dto.getStatus() != null)             p.setStatus(dto.getStatus());
        if (dto.getBusinessType() != null)       p.setBusinessType(dto.getBusinessType());
        p.setLocation(dto.getLocation());
        p.setDescription(dto.getDescription());
        p.setClientGstNo(dto.getClientGstNo());
        p.setClientAddress(dto.getClientAddress());
        p.setBillingAddress(dto.getBillingAddress());
        p.setPoWoStatus(dto.getPoWoStatus());
        p.setGstPct(dto.getGstPct());
        p.setQuoteGstPct(dto.getQuoteGstPct());
        if (dto.getQuoteGross() != null)         p.setQuoteGross(dto.getQuoteGross());
        p.setQuoteValue(dto.getQuoteValue());
        p.setTotalValue(dto.getTotalValue());
        if (dto.getCollectionReceived() != null) p.setCollectionReceived(dto.getCollectionReceived());
        if (dto.getExpenses() != null) {
            ProjectDto.Expenses e = dto.getExpenses();
            if (e.getMaterial() != null)      p.setExpMaterial(e.getMaterial());
            if (e.getLabour() != null)        p.setExpLabour(e.getLabour());
            if (e.getSubcontract() != null)   p.setExpSubcontract(e.getSubcontract());
            if (e.getConsultants() != null)   p.setExpConsultants(e.getConsultants());
            if (e.getMiscellaneous() != null) p.setExpMiscellaneous(e.getMiscellaneous());
        }
    }
}
