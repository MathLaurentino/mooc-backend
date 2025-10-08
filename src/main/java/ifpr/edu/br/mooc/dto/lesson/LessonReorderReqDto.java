package ifpr.edu.br.mooc.dto.lesson;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record LessonReorderReqDto(
        @NotEmpty(message = "A lista de aulas não pode estar vazia")
        @Valid
        List<LessonOrderItem> aulas
) {
    public record LessonOrderItem(
            @NotNull(message = "ID da aula é obrigatório")
            Long id,

            @NotNull(message = "Ordem da aula é obrigatória")
            @Positive(message = "Ordem deve ser um número positivo")
            Integer ordemAula
    ) {}
}