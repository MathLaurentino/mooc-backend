package ifpr.edu.br.mooc.dto.user;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UpdateUserRequest(
        @NotBlank(message = "Nome completo é obrigatório")
        @Size(min = 3, max = 255)
        String fullName,

        @NotBlank(message = "CPF é obrigatório")
        @CPF
        String cpf,

        @NotNull(message = "Data de nascimento é obrigatório")
        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate birthDate,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email
) {}
