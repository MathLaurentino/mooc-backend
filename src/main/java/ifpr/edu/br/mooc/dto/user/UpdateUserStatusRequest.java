package ifpr.edu.br.mooc.dto.user;

import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull(message = "{user.active.notnull}")
        Boolean active
) {}