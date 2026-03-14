package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "Email is required")
        @Email(message = "The email format is invalid")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {

}
