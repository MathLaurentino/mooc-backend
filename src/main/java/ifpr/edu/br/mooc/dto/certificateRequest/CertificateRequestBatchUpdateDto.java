package ifpr.edu.br.mooc.dto.certificateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record CertificateRequestBatchUpdateDto(
        @NotBlank(message = "{certificateBatch.status.notblank}")
        @Pattern(regexp = "aprovado|reprovado", message = "{certificateBatch.status.pattern}")
        String status,

        @NotEmpty(message = "{certificateBatch.requests.notempty}")
        @Valid
        List<RequestUpdate> requests
) {
    public record RequestUpdate(
            @NotNull(message = "{certificateRequest.requestId.notnull}")
            String requestId
    ) {}
}