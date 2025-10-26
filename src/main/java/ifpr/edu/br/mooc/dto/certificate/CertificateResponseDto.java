package ifpr.edu.br.mooc.dto.certificate;

import java.time.LocalDateTime;

public record CertificateResponseDto(
        Long id,
        Long enrollmentId,
        String studentName,
        String studentCpf,
        String courseName,
        String workload,
        String campusName,
        LocalDateTime completionDate,
        String documentHash,
        String digitalSignature,
        String publicKey,
        String certificateUrl
) {
}