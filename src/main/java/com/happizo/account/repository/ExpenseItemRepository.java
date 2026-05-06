package com.happizo.account.repository;

import com.happizo.account.entity.ExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Long> {

    List<ExpenseItem> findByProjectIdAndCategoryOrderById(Long projectId, String category);

    List<ExpenseItem> findByProjectIdOrderByCategoryAscIdAsc(Long projectId);

    @Query("SELECT COALESCE(SUM(e.pwjTotalPayable), 0) FROM ExpenseItem e WHERE e.projectId = :projectId AND e.category = :category")
    BigDecimal sumPwjTotalPayable(Long projectId, String category);

    @Query("SELECT COALESCE(SUM(e.vendorTotalPayable), 0) FROM ExpenseItem e WHERE e.projectId = :projectId AND e.category = :category")
    BigDecimal sumVendorTotalPayable(Long projectId, String category);

    @Query("SELECT COALESCE(SUM(e.vendorGstAmount), 0) FROM ExpenseItem e WHERE e.projectId = :projectId AND e.category = :category")
    BigDecimal sumVendorGst(Long projectId, String category);

    @Query("SELECT COALESCE(SUM(e.paidAmount), 0) FROM ExpenseItem e WHERE e.projectId = :projectId AND e.category = :category")
    BigDecimal sumPaid(Long projectId, String category);

    // Returns [category, sumPwjTotal, sumPaid, sumVendorTotal] grouped by category
    @Query("""
        SELECT e.category,
               COALESCE(SUM(e.pwjTotalPayable), 0),
               COALESCE(SUM(e.paidAmount), 0),
               COALESCE(SUM(e.vendorTotalPayable), 0)
        FROM ExpenseItem e
        WHERE e.projectId = :projectId
        GROUP BY e.category
        """)
    List<Object[]> getSummaryByProject(Long projectId);
}
