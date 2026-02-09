package br.com.brevus.commerce_api.dto;

import br.com.brevus.commerce_api.enums.SaleStatus;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SaleRequestDTO(
        UUID id,

        @NotNull(message = "Sale date is required.")
        @PastOrPresent(message = "Sale date cannot be in the future.")
        LocalDateTime saleDate,

        @NotNull(message = "Sale status is required.")
        SaleStatus status,

        @NotNull(message = "Total amount is required.")
        @PositiveOrZero(message = "Total amount must be zero or a positive value.")
        @Digits(integer = 10, fraction = 2, message = "Total amount must have a maximum of 2 decimal places.")
        BigDecimal totalAmount,

        @NotNull(message = "Client ID is required.")
        UUID clientId,

        @NotNull(message = "Seller ID is required.")
        UUID sellerId
) {
}
