package ifpr.edu.br.mooc.dto.autoApprove;

import jakarta.validation.constraints.NotNull;

public record AutoApproveUpdateDto(
        @NotNull(message = "{autoApprove.enabled.notnull}")
        Boolean enabled
) {}