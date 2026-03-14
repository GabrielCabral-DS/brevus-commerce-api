package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserProfileRequestDTO(

        @NotBlank(message = "O nome completo precisa ser informado")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "O número de telefone é obrigatório")
        @Pattern(regexp = "^\\d{10,15}$", message = "O telefone deve conter apenas números (10 a 15 dígitos)")
        String phone,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve ser uma data passada")
        LocalDate dateBirth
) {
}
