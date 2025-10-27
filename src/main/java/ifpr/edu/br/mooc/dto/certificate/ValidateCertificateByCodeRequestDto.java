package ifpr.edu.br.mooc.dto.certificate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ValidateCertificateByCodeRequestDto(
        @NotBlank(message = "Código do certificado é obrigatório")
        @Size(min = 10, message = "Código deve ter no mínimo 10 caracteres")
        String certificateCode
) {
}