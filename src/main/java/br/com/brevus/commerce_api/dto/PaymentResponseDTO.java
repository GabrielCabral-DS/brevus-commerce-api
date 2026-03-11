package br.com.brevus.commerce_api.dto;

import br.com.brevus.commerce_api.enums.PaymentMethod;
import br.com.brevus.commerce_api.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponseDTO(

        UUID id,
        UUID saleId,

        PaymentMethod method,
        PaymentStatus status,
        String gatewayTransactionId,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime confirmedAt
) {
}
