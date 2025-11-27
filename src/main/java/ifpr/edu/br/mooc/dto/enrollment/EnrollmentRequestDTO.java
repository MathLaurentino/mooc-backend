package ifpr.edu.br.mooc.dto.enrollment;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequestDTO(
        @NotNull(message = "{enrollment.courseId.notnull}")
        Long cursoId
) {}