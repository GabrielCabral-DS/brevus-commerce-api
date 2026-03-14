package br.com.brevus.commerce_api.dto;
import java.math.BigDecimal;
import java.util.UUID;

public record SaleItemResponseDTO(

        UUID id,
        UUID saleId,
        UUID productId,
        String productName,
        String description,
        String productImagePath,

        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
