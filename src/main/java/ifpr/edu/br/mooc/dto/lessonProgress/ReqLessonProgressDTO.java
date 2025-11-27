package ifpr.edu.br.mooc.dto.lessonProgress;

import jakarta.validation.constraints.NotNull;

public record ReqLessonProgressDTO(
        @NotNull(message = "{lessonProgress.completed.notnull}")
        boolean concluido
) {}