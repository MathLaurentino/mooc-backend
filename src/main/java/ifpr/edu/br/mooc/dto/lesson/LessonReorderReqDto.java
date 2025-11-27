package ifpr.edu.br.mooc.dto.lesson;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record LessonReorderReqDto(
        @NotEmpty(message = "{lesson.reorder.list.notempty}")
        @Valid
        List<LessonOrderItem> aulas
) {
    public record LessonOrderItem(
            @NotNull(message = "{lesson.reorder.id.notnull}")
            Long id,

            @NotNull(message = "{lesson.reorder.order.notnull}")
            @Positive(message = "{lesson.reorder.order.positive}")
            Integer ordemAula
    ) {}
}