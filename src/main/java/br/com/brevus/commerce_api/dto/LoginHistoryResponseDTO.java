package br.com.brevus.commerce_api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginHistoryResponseDTO(

        UUID userId,
        String email,
        LocalDateTime loginAt
) {
}
