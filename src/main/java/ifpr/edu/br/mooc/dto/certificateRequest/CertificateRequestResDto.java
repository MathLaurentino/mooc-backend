package ifpr.edu.br.mooc.dto.certificateRequest;

import java.time.LocalDateTime;

public record CertificateRequestResDto(
        String id,
        Long enrollmentId,
        String status,
        String statusDescription,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String rejectionReason,
        StudentInfo student,
        CourseInfo course
) {
    public record StudentInfo(
            Long id,
            String fullName,
            String cpf,
            String email
    ) {}

    public record CourseInfo(
            Long id,
            String name,
            Integer workload,
            String campusName
    ) {}
}