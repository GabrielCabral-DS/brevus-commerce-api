package br.com.brevus.commerce_api.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.UUID;

public record UserRequestDTO(

        UUID id,
        @NotBlank(message = "O nome completo precisa ser informado")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%?&_])(?!.*[@$!%?&_].*[@$!%?&_].*[@$!%?&_])[A-Za-z\\d@$!%?&_]{8,}$",
                message = "A senha deve conter pelo menos: uma letra maiúscula, uma letra minúscula, um número e até dois caracteres especiais (@, $, !, %, ?, &, _). Exemplo: Teste@0000."
        )
        String password,

        @NotBlank(message = "O número de telefone é obrigatório")
        @Pattern(regexp = "^\\d{10,15}$", message = "O telefone deve conter apenas números (10 a 15 dígitos)")
        String phone,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve ser uma data passada")
        LocalDate dateBirth,

        @PastOrPresent(message = "A data de registro não pode estar no futuro")
        LocalDate dateRegistered

) {
}
