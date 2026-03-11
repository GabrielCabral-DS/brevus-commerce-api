package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record SaleRequestDTO(

        List<SaleItemRequestDTO> items,

        @NotNull(message = "Client ID is required.")
        UUID clientId,

        @NotNull(message = "Address ID is required.")
        UUID addressId,

        @NotNull(message = "Seller ID is required.")
        UUID sellerId
) {
}
