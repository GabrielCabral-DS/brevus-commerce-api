package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginRequestDTO {

        @NotBlank(message = "Email is required")
        @Email(message = "The email format is invalid")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
}
