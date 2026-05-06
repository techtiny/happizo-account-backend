package com.happizo.account.service;

import com.happizo.account.dto.DashboardStatsDto;
import com.happizo.account.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class DashboardService {

    private static final BigDecimal BUDGET_RATIO = new BigDecimal("0.80");
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final ProjectRepository projectRepository;

    public DashboardService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public DashboardStatsDto getStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        BigDecimal totalQuote      = projectRepository.sumQuoteGross();
        BigDecimal totalCollection = projectRepository.sumCollectionReceived();
        BigDecimal totalSpent      = projectRepository.sumTotalExpenses();
        BigDecimal totalBudget     = totalQuote.multiply(BUDGET_RATIO);

        stats.setTotalQuote(totalQuote);
        stats.setTotalBudget(totalBudget);
        stats.setTotalSpent(totalSpent);
        stats.setTotalCollection(totalCollection);
        stats.setTotalProjects(projectRepository.count());
        stats.setActiveProjects(projectRepository.countByStatus("active"));
        stats.setCompletedProjects(projectRepository.countByStatus("completed"));

        BigDecimal profit = totalQuote.subtract(totalSpent);
        BigDecimal margin = totalQuote.compareTo(BigDecimal.ZERO) > 0
                ? profit.divide(totalQuote, 4, RoundingMode.HALF_UP).multiply(HUNDRED)
                : BigDecimal.ZERO;
        stats.setAvgProfitMargin(margin.setScale(1, RoundingMode.HALF_UP));

        stats.setCategoryBreakdown(buildCategoryBreakdown());
        stats.setProjectExpenses(buildProjectExpenses());

        return stats;
    }

    private List<Map<String, Object>> buildCategoryBreakdown() {
        List<Object[]> rows = projectRepository.findExpenseBreakdownByProject();
        BigDecimal mat = BigDecimal.ZERO, lab = BigDecimal.ZERO,
                   sub = BigDecimal.ZERO, con = BigDecimal.ZERO, mis = BigDecimal.ZERO;

        for (Object[] r : rows) {
            mat = mat.add(dec(r[1]));
            lab = lab.add(dec(r[2]));
            sub = sub.add(dec(r[3]));
            con = con.add(dec(r[4]));
            mis = mis.add(dec(r[5]));
        }

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map("name", "MATERIAL",     "value", mat));
        list.add(map("name", "LABOUR",       "value", lab));
        list.add(map("name", "SUBCONTRACT",  "value", sub));
        list.add(map("name", "CONSULTANTS",  "value", con));
        list.add(map("name", "MISCELLANEOUS","value", mis));
        return list;
    }

    private List<Map<String, Object>> buildProjectExpenses() {
        List<Object[]> rows = projectRepository.findExpenseBreakdownByProject();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] r : rows) {
            BigDecimal mat = dec(r[1]), lab = dec(r[2]), sub = dec(r[3]),
                       con = dec(r[4]), mis = dec(r[5]);
            BigDecimal total = mat.add(lab).add(sub).add(con).add(mis);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name",          r[0]);
            item.put("MATERIAL",      mat);
            item.put("LABOUR",        lab);
            item.put("SUBCONTRACTOR", sub);
            item.put("OVERHEAD",      con.add(mis));
            item.put("total",         total);
            list.add(item);
        }
        return list;
    }

    private Map<String, Object> map(String k1, Object v1, String k2, Object v2) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        return m;
    }

    private BigDecimal dec(Object o) {
        if (o == null) return BigDecimal.ZERO;
        if (o instanceof BigDecimal) return (BigDecimal) o;
        return new BigDecimal(o.toString());
    }
}
