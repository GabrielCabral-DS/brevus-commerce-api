package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.enums.PaymentStatus;
import br.com.brevus.commerce_api.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    boolean existsBySaleId(UUID saleId);

    List<Payment> findTop10ByStatusOrderByCreatedAtDesc(PaymentStatus status);

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.status = :status
    AND p.createdAt BETWEEN :startDate AND :endDate
""")
    BigDecimal sumPaymentsByStatusAndPeriod(
            @Param("status") PaymentStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
