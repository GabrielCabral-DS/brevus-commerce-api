package br.com.brevus.commerce_api.dto;

import br.com.brevus.commerce_api.enums.PaymentMethod;
import br.com.brevus.commerce_api.enums.PaymentStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentRequestDTO(

        UUID id,

        @NotNull(message = "Sale ID is required.")
        UUID saleId,

        @NotNull(message = "Payment method is required.")
        PaymentMethod method,

        @NotNull(message = "Payment status is required.")
        PaymentStatus status,

        @NotBlank(message = "Transaction ID (txid) is required.")
        @Size(max = 100, message = "Transaction ID cannot exceed 100 characters.")
        String txid,

        @NotNull(message = "Payment amount is required.")
        @Positive(message = "Payment amount must be greater than zero.")
        @Digits(integer = 10, fraction = 2, message = "Amount must have a maximum of 2 decimal places.")
        BigDecimal amount,

        @PastOrPresent(message = "Creation date cannot be in the future.")
        LocalDateTime createdAt,

        @PastOrPresent(message = "Confirmation date cannot be in the future.")
        LocalDateTime confirmedAt
) {
}
