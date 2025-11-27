package ifpr.edu.br.mooc.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "{login.email.notblank}")
        @Email(message = "{login.email.invalid}")
        String email,

        @NotBlank(message = "{login.password.notblank}")
        String password
) {}