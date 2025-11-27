package ifpr.edu.br.mooc.dto.user;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank(message = "{user.fullName.notblank}")
        @Size(min = 3, max = 255, message = "{user.fullName.size}")
        String fullName,

        @NotBlank(message = "{user.cpf.notblank}")
        @CPF(message = "{user.cpf.invalid}")
        String cpf,

        @NotNull(message = "{user.birthDate.notnull}")
        @Past(message = "{user.birthDate.past}")
        LocalDate birthDate,

        @NotBlank(message = "{user.email.notblank}")
        @Email(message = "{user.email.invalid}")
        String email,

        @NotBlank(message = "{user.password.notblank}")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
                message = "{user.password.pattern}"
        )
        String password
) {}