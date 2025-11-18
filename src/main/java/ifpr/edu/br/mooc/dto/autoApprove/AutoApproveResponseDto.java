package ifpr.edu.br.mooc.dto.autoApprove;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AutoApproveResponseDto(
        Boolean enabled,
        LocalDateTime enabledAt,
        LocalDateTime disabledAt
) {
}