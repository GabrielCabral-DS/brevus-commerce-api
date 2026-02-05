package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RoleRequestDTO(

        UUID id,

        @NotBlank(message = "A role must be informed")
        String name

) {
}
