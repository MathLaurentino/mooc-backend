package ifpr.edu.br.mooc.dto.certificateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CertificateRequestUpdateDto(
        @NotBlank(message = "Status é obrigatório")
        @Pattern(regexp = "aprovado|reprovado", message = "Status deve ser 'aprovado' ou 'reprovado'")
        String status,

        String motivoReprovacao
) {
}