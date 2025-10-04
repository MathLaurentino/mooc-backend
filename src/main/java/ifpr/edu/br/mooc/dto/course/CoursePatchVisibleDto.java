package ifpr.edu.br.mooc.dto.course;

import jakarta.validation.constraints.NotNull;

public record CoursePatchVisibleDto(
        @NotNull
        boolean visible
){
}
