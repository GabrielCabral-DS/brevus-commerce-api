package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RecoverPasswordEmailRequestDTO(

        @NotBlank(message = "Token inválido ou ausente")
        String token,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%?&_])(?!.*[@$!%?&_].*[@$!%?&_].*[@$!%?&_])[A-Za-z\\d@$!%?&_]{8,}$",
                message = "A senha deve conter pelo menos: uma letra maiúscula, uma letra minúscula, um número e até dois caracteres especiais (@, $, !, %, ?, &, _). Exemplo: Teste@0000."
        )
        String password,

        @NotBlank(message = "A confirmação de senha é obrigatória.")
        String passwordConfirmation
) {
}
