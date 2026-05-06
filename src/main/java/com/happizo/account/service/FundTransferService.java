package com.happizo.account.service;

import com.happizo.account.dto.FundTransferDto;
import com.happizo.account.entity.FundTransfer;
import com.happizo.account.entity.Project;
import com.happizo.account.repository.FundTransferRepository;
import com.happizo.account.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class FundTransferService {

    private final FundTransferRepository transferRepo;
    private final ProjectRepository projectRepo;

    public FundTransferService(FundTransferRepository transferRepo, ProjectRepository projectRepo) {
        this.transferRepo = transferRepo;
        this.projectRepo  = projectRepo;
    }

    // ── List all transfers ────────────────────────────────────────────────
    public List<FundTransferDto> getAll() {
        Map<Long, String> names = new HashMap<>();
        projectRepo.findAll().forEach(p -> names.put(p.getId(), p.getName()));
        return transferRepo.findAllByOrderByTransferDateDescIdDesc()
                .stream().map(t -> toDto(t, names)).toList();
    }

    // ── Available balance for a project ──────────────────────────────────
    public BigDecimal availableBalance(Long projectId) {
        Project p = projectRepo.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));
        BigDecimal collected  = safe(p.getCollectionReceived());
        BigDecimal outgoing   = transferRepo.sumOutgoing(projectId);
        BigDecimal incoming   = transferRepo.sumIncoming(projectId);
        return collected.subtract(outgoing).add(incoming);
    }

    // ── Create transfer ───────────────────────────────────────────────────
    public FundTransferDto create(FundTransferDto dto) {
        Project from = projectRepo.findById(dto.getFromProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Source project not found"));
        Project to = projectRepo.findById(dto.getToProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Destination project not found"));

        // Rule: both must be B2B
        if (!"B2B".equals(from.getBusinessType()))
            throw new IllegalStateException("Source project is not B2B. Transfers only allowed between B2B projects.");
        if (!"B2B".equals(to.getBusinessType()))
            throw new IllegalStateException("Destination project is not B2B. Transfers only allowed between B2B projects.");

        // Rule: cannot transfer to same project
        if (from.getId().equals(to.getId()))
            throw new IllegalStateException("Source and destination project cannot be the same.");

        // Rule: amount must be positive
        BigDecimal amount = dto.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalStateException("Transfer amount must be greater than zero.");

        // Rule: cannot exceed available balance
        BigDecimal available = availableBalance(from.getId());
        if (amount.compareTo(available) > 0)
            throw new IllegalStateException(
                String.format("Insufficient funds. Available balance: ₹%.0f, Requested: ₹%.0f",
                        available, amount));

        FundTransfer t = new FundTransfer();
        t.setFromProjectId(from.getId());
        t.setToProjectId(to.getId());
        t.setAmount(amount);
        t.setTransferDate(dto.getTransferDate() != null ? dto.getTransferDate() : LocalDate.now());
        t.setRemarks(dto.getRemarks());

        Map<Long, String> names = Map.of(from.getId(), from.getName(), to.getId(), to.getName());
        return toDto(transferRepo.save(t), names);
    }

    // ── Delete transfer ───────────────────────────────────────────────────
    public void delete(Long id) {
        if (!transferRepo.existsById(id))
            throw new IllegalArgumentException("Transfer not found: " + id);
        transferRepo.deleteById(id);
    }

    // ── B2B projects only ─────────────────────────────────────────────────
    public List<Map<String, Object>> getB2bProjects() {
        List<Map<String, Object>> result = new ArrayList<>();
        projectRepo.findAll().forEach(p -> {
            if ("B2B".equals(p.getBusinessType())) {
                BigDecimal available = availableBalance(p.getId());
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id",            p.getId());
                m.put("name",          p.getName());
                m.put("status",        p.getStatus());
                m.put("businessType",  p.getBusinessType());
                m.put("collectionReceived", safe(p.getCollectionReceived()));
                m.put("availableBalance",   available);
                result.add(m);
            }
        });
        return result;
    }

    private FundTransferDto toDto(FundTransfer t, Map<Long, String> names) {
        FundTransferDto d = new FundTransferDto();
        d.setId(t.getId());
        d.setFromProjectId(t.getFromProjectId());
        d.setFromProjectName(names.getOrDefault(t.getFromProjectId(), "Unknown"));
        d.setToProjectId(t.getToProjectId());
        d.setToProjectName(names.getOrDefault(t.getToProjectId(), "Unknown"));
        d.setAmount(t.getAmount());
        d.setTransferDate(t.getTransferDate());
        d.setRemarks(t.getRemarks());
        return d;
    }

    private BigDecimal safe(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}
