package ifpr.edu.br.mooc.dto.certificateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record CertificateRequestBatchUpdateDto(
        @NotBlank(message = "Status é obrigatório")
        @Pattern(regexp = "aprovado|reprovado", message = "Status deve ser 'aprovado' ou 'reprovado'")
        String status,

        @NotEmpty(message = "A lista de solicitações não pode estar vazia")
        @Valid
        List<RequestUpdate> requests
) {
    public record RequestUpdate(
            @NotNull(message = "ID da solicitação é obrigatório")
            String requestId
    ) {}
}