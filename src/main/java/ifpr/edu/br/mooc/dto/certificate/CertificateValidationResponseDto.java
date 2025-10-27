package ifpr.edu.br.mooc.dto.certificate;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CertificateValidationResponseDto(
        Boolean isValid,
        String message,
        
        // Dados do certificado (apenas se válido)
        String studentName,
        String studentCpf,
        String courseName,
        String workload,
        String campusName,
        LocalDateTime completionDate
) {
    // Construtor para certificado válido
    public static CertificateValidationResponseDto valid(
            String studentName,
            String studentCpf,
            String courseName,
            String workload,
            String campusName,
            LocalDateTime completionDate
    ) {
        return new CertificateValidationResponseDto(
                true,
                "Certificado válido e autêntico",
                studentName,
                studentCpf,
                courseName,
                workload,
                campusName,
                completionDate
        );
    }

    // Construtor para certificado inválido
    public static CertificateValidationResponseDto invalid(String message) {
        return new CertificateValidationResponseDto(
                false,
                message,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}