package br.com.brevus.commerce_api.dto;


import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequestDTO(
        UUID id,

        String name,
        BigDecimal price,
        Integer stockQuantity,
        UUID categoryId
) {
}
