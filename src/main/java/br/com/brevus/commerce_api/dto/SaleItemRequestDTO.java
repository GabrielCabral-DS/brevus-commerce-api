package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record SaleItemRequestDTO(

        UUID id,

        @NotNull(message = "Sale ID is required.")
        UUID saleId,

        @NotNull(message = "Product ID is required.")
        UUID productId,

        @NotNull(message = "Quantity is required.")
        @Min(value = 1, message = "Quantity must be at least 1.")
        Integer quantity,

        @NotNull(message = "Unit price is required.")
        @Positive(message = "Unit price must be greater than zero.")
        @Digits(integer = 10, fraction = 2, message = "Unit price must have up to 2 decimal places.")
        BigDecimal unitPrice,

        @NotNull(message = "Subtotal is required.")
        @Positive(message = "Subtotal must be greater than zero.")
        @Digits(integer = 10, fraction = 2, message = "Subtotal must have up to 2 decimal places.")
        BigDecimal subtotal
) {
}
