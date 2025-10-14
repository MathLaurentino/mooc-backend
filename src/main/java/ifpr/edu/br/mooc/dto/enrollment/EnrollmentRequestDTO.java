package ifpr.edu.br.mooc.dto.enrollment;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequestDTO(
    @NotNull(message = "O ID do curso é obrigatório")
    Long cursoId
) { }