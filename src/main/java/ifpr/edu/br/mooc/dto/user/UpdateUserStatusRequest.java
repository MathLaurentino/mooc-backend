package ifpr.edu.br.mooc.dto.user;

import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull(message = "Status é obrigatório")
        Boolean active
) {
}