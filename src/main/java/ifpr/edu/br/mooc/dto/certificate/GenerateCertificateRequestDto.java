package ifpr.edu.br.mooc.dto.certificate;

import jakarta.validation.constraints.NotNull;

public record GenerateCertificateRequestDto(
        @NotNull(message = "ID da inscrição é obrigatório")
        Long enrollmentId
) {
}