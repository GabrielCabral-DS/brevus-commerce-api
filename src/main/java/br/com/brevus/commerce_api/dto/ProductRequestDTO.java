package br.com.brevus.commerce_api.dto;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequestDTO(
        UUID id,

        @NotBlank(message = "Product name is required.")
        @Size(min = 2, max = 150, message = "The name must be between 2 and 150 characters.")
        String name,

        @NotNull(message = "Price is required.")
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01.")
        @Digits(integer = 10, fraction = 2, message = "Price must have a maximum of 2 decimal places.")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required.")
        @Min(value = 0, message = "Stock cannot be negative.")
        Integer stockQuantity,

        @NotNull(message = "Category ID is required.")
        UUID categoryId
) {
}
