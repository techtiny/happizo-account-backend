package com.happizo.account.repository;

import com.happizo.account.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(String status);

    @Query("SELECT MAX(CAST(SUBSTRING(p.orderNumber, 8) AS int)) FROM Project p WHERE p.orderNumber IS NOT NULL")
    Integer findMaxOrderSequence();

    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(p.quoteGross), 0) FROM Project p")
    BigDecimal sumQuoteGross();

    @Query("SELECT COALESCE(SUM(p.collectionReceived), 0) FROM Project p")
    BigDecimal sumCollectionReceived();

    @Query("""
        SELECT COALESCE(SUM(p.expMaterial + p.expLabour + p.expSubcontract
               + p.expConsultants + p.expMiscellaneous), 0)
        FROM Project p
        """)
    BigDecimal sumTotalExpenses();

    @Query("""
        SELECT p.name,
               p.expMaterial,
               p.expLabour,
               p.expSubcontract,
               p.expConsultants,
               p.expMiscellaneous
        FROM Project p
        WHERE p.quoteGross > 0
        ORDER BY p.quoteGross DESC
        """)
    List<Object[]> findExpenseBreakdownByProject();
}
