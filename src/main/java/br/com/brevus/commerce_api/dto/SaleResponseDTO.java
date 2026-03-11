package br.com.brevus.commerce_api.dto;

import br.com.brevus.commerce_api.enums.DeliveryStatus;
import br.com.brevus.commerce_api.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SaleResponseDTO (

        UUID id,
        LocalDateTime saleDate,
        SaleStatus status,
        BigDecimal totalAmount,
        DeliveryStatus deliveryStatus,

        UUID clientId,
        UUID addressId,
        UUID sellerId,

        List<SaleItemResponseDTO> items,
        PaymentRequestDTO payment

) {

}
