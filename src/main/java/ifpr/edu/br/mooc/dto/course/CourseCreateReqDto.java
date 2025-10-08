package ifpr.edu.br.mooc.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CourseCreateReqDto(
        @NotBlank
        String nome,

        @NotBlank
        String descricao,

        @NotNull
        Long areaConhecimentoId,

        @NotNull
        Long campusId,

        @NotBlank
        String nomeProfessor,

        @NotNull
        @Positive
        Integer cargaHoraria
) {}