package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryRequestDTO(

         UUID id,
         @NotBlank(message = "required field")
         @Size(min = 3, message = "The name must have at least three characters.")
         String name

) {
}
