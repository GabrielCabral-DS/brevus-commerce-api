package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(

        @NotBlank(message = "The refresh token must be provided")
        String refreshToken) {}
