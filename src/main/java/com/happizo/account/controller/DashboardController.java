package com.happizo.account.controller;

import com.happizo.account.dto.DashboardStatsDto;
import com.happizo.account.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    @GetMapping("/project-expenses")
    public ResponseEntity<List<Map<String, Object>>> getProjectExpenses() {
        return ResponseEntity.ok(dashboardService.getStats().getProjectExpenses());
    }
}
