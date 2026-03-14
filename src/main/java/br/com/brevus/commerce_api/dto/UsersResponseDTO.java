package br.com.brevus.commerce_api.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UsersResponseDTO(

        UUID id,
        String name,
        String email,
        String phone,
        LocalDate dateBirth,
        LocalDate dateRegistered
) {
}
