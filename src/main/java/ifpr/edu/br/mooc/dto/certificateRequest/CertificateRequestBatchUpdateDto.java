package ifpr.edu.br.mooc.dto.certificateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CertificateRequestBatchUpdateDto(
        @NotEmpty(message = "A lista de solicitações não pode estar vazia")
        @Valid
        List<RequestUpdate> requests
) {
    public record RequestUpdate(
            @NotNull(message = "ID da solicitação é obrigatório")
            String requestId,

            @NotNull(message = "Status é obrigatório")
            String status,

            String motivoReprovacao
    ) {}
}