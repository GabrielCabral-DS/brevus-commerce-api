package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId);

}
