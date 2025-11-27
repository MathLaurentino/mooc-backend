package ifpr.edu.br.mooc.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CourseCreateReqDto(
        @NotBlank(message = "{course.name.notblank}")
        String nome,

        @NotBlank(message = "{course.description.notblank}")
        String descricao,

        @NotNull(message = "{course.knowledgeAreaId.notnull}")
        Long areaConhecimentoId,

        @NotNull(message = "{course.campusId.notnull}")
        Long campusId,

        @NotBlank(message = "{course.professorName.notblank}")
        String nomeProfessor,

        @NotNull(message = "{course.workload.notnull}")
        @Positive(message = "{course.workload.positive}")
        Integer cargaHoraria
) {}