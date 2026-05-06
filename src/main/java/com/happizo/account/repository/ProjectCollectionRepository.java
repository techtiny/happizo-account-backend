package com.happizo.account.repository;

import com.happizo.account.entity.ProjectCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ProjectCollectionRepository extends JpaRepository<ProjectCollection, Long> {

    List<ProjectCollection> findByProjectIdOrderByStageAsc(Long projectId);

    // Used by daily scheduler — respects alertActive flag and paymentDate window
    @Query("SELECT c FROM ProjectCollection c WHERE c.alertActive = true AND c.paymentDate IS NOT NULL AND c.paymentDate >= :today")
    List<ProjectCollection> findActiveAlerts(@Param("today") LocalDate today);

    // Used by manual trigger — all with paymentDate set, regardless of alertActive/interval
    @Query("SELECT c FROM ProjectCollection c WHERE c.paymentDate IS NOT NULL")
    List<ProjectCollection> findAllWithPaymentDate();
}
