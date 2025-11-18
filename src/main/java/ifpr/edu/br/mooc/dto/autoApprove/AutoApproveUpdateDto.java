package ifpr.edu.br.mooc.dto.autoApprove;

import jakarta.validation.constraints.NotNull;

public record AutoApproveUpdateDto(
        @NotNull(message = "O campo 'enabled' é obrigatório")
        Boolean enabled
) {
}