package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserRoleRequestDTO(

        UUID id,

        @NotNull(message = "The field cannot be null.")
        UUID userId,

        @NotNull(message = "The field cannot be null.")
        UUID roleId
) {
}
