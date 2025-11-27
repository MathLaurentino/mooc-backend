package ifpr.edu.br.mooc.dto.certificateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CertificateRequestUpdateDto(
        @NotBlank(message = "{certificateRequest.status.notblank}")
        @Pattern(regexp = "aprovado|reprovado", message = "{certificateRequest.status.pattern}")
        String status,

        String motivoReprovacao
) {}