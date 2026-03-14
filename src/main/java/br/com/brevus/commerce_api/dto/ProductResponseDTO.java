package br.com.brevus.commerce_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String name,
        BigDecimal price,
        Integer stockQuantity,
        String imagePath,
        UUID categoryId,
        String categoryName,
        String description,
        LocalDateTime createdAt
) {}