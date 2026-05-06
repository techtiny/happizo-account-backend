package com.happizo.account.controller;

import com.happizo.account.dto.ExpenseItemDto;
import com.happizo.account.service.ExpenseItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseItemController {

    private final ExpenseItemService service;

    public ExpenseItemController(ExpenseItemService service) {
        this.service = service;
    }

    @GetMapping("/{projectId}/{category}")
    public ResponseEntity<List<ExpenseItemDto>> getItems(
            @PathVariable Long projectId,
            @PathVariable String category) {
        return ResponseEntity.ok(service.getByProjectAndCategory(projectId, category));
    }

    @GetMapping("/{projectId}/{category}/summary")
    public ResponseEntity<Map<String, Object>> getSummary(
            @PathVariable Long projectId,
            @PathVariable String category) {
        return ResponseEntity.ok(service.getSummary(projectId, category));
    }

    @PostMapping
    public ResponseEntity<ExpenseItemDto> create(@RequestBody ExpenseItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseItemDto> update(@PathVariable Long id, @RequestBody ExpenseItemDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}
