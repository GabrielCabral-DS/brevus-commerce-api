package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecoverPasswordRequestDTO(

        @Email
        @NotBlank(message = "O email deve ser informado")
        String email
) {
}
