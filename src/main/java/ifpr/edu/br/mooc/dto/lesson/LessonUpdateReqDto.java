package ifpr.edu.br.mooc.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LessonUpdateReqDto(
        @NotBlank(message = "{lesson.title.notblank}")
        @Size(max = 100, message = "{lesson.title.size}")
        String titulo,

        @NotBlank(message = "{lesson.description.notblank}")
        String descricao,

        @NotBlank(message = "{lesson.videoUrl.notblank}")
        String urlVideo
) {}