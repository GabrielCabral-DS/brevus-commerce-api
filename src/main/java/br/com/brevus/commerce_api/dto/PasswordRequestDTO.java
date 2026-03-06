package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordRequestDTO(

        @NotBlank(message = "A senha atual deve ser informada para que possa atualizar a senha!")
        String currentPassword,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%?&_])[A-Za-z\\d@$!%?&_]{8,}$",
        message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial."
        )
        String password,

        @NotBlank(message = "A confirmação de senha é obrigatória.")
        String passwordConfirmation
) {
}
