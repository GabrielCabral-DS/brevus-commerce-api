package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record UserRequestDTO(

        UUID id,

        @NotBlank(message = "The name must be provided")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must have at least 8 characters")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%?&_])(?!.*[@$!%?&_].*[@$!%?&_].*[@$!%?&_])[A-Za-z\\d@$!%?&_]{8,}$",
                message = "The password must contain at least: one uppercase letter, one lowercase letter, one number, and up to two special characters (@, $, !, %, ?, &, _). Example: Test@0000."
        )
        String password,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\d{10,15}$", message = "Phone must contain only numbers (10 to 15 digits)")
        String phone,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate dateBirth,

        @PastOrPresent(message = "Registration date cannot be in the future")
        LocalDate dateRegistered

) {
}
