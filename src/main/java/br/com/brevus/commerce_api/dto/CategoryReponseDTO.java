package br.com.brevus.commerce_api.dto;


import java.util.UUID;

public record CategoryReponseDTO(
        UUID id,
        String name
) {
}
