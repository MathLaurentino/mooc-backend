package ifpr.edu.br.mooc.dto.course;

import jakarta.validation.constraints.NotNull;

public record CoursePatchVisibleDto(
        @NotNull(message = "{course.visible.notnull}")
        Boolean visivel
) {}