package ifpr.edu.br.mooc.dto.certificate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ValidateCertificateByCodeRequestDto(
        @NotBlank(message = "{certificate.code.notblank}")
        @Size(min = 10, message = "{certificate.code.size}")
        String certificateCode
) {}