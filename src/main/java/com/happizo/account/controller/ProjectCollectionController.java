package com.happizo.account.controller;

import com.happizo.account.dto.ProjectCollectionDto;
import com.happizo.account.service.CollectionAlertService;
import com.happizo.account.service.ProjectCollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/collections")
public class ProjectCollectionController {

    private final ProjectCollectionService service;
    private final CollectionAlertService alertService;

    public ProjectCollectionController(ProjectCollectionService service,
                                       CollectionAlertService alertService) {
        this.service      = service;
        this.alertService = alertService;
    }

    @GetMapping("/project/{projectId}")
    public List<ProjectCollectionDto> getByProject(@PathVariable Long projectId) {
        return service.getByProject(projectId);
    }

    @PostMapping
    public ProjectCollectionDto create(@RequestBody ProjectCollectionDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ProjectCollectionDto update(@PathVariable Long id, @RequestBody ProjectCollectionDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/trigger-alerts")
    public ResponseEntity<Map<String, Object>> triggerAlerts() {
        int sent = alertService.triggerNow();
        return ResponseEntity.ok(Map.of(
            "status", "ok",
            "sent", sent,
            "message", sent > 0 ? sent + " alert email(s) sent to tshobana1@gmail.com" : "No collections with payment date found"
        ));
    }
}
