package ifpr.edu.br.mooc.dto.user;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank
        @Size(min = 3, max = 255)
        String fullName,

        @NotBlank
        @CPF
        String cpf,

        @NotNull
        @Past
        LocalDate birthDate,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, incluindo uma letra maiúscula e um número."
        )
        String password
) {}