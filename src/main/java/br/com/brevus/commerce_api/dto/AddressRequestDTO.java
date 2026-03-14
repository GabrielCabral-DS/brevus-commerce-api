package br.com.brevus.commerce_api.dto;

import br.com.brevus.commerce_api.enums.State;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AddressRequestDTO(

        @NotNull(message = "User ID is required.")
        UUID userId,

        @NotBlank(message = "Street is required.")
        @Size(max = 150, message = "Street must have at most 150 characters.")
        String street,

        @NotBlank(message = "Number is required.")
        @Size(max = 10, message = "Number must have at most 10 characters.")
        String number,

        @Size(max = 100, message = "Complement must have at most 100 characters.")
        String complement,

        @NotBlank(message = "Neighborhood is required.")
        @Size(max = 100, message = "Neighborhood must have at most 100 characters.")
        String neighborhood,

        @NotNull(message = "City is required.")
        String  city,

        @NotNull(message = "State is required.")
        State state,

        @NotBlank(message = "Zip code is required.")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Zip code must be in format 00000-000")
        String zipCode

) {
}