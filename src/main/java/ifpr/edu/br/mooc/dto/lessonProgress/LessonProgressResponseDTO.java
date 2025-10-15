package ifpr.edu.br.mooc.dto.lessonProgress;

import ifpr.edu.br.mooc.entity.LessonProgress;

import java.time.LocalDateTime;

public record LessonProgressResponseDTO(
        Long id,
        Long inscricaoId,
        Long aulaId,
        Boolean concluido,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static LessonProgressResponseDTO fromEntity(LessonProgress lessonProgress) {
        return new LessonProgressResponseDTO(
                lessonProgress.getId(),
                lessonProgress.getEnrollment().getId(),
                lessonProgress.getLesson().getId(),
                lessonProgress.getCompleted(),
                lessonProgress.getCreatedAt(),
                lessonProgress.getUpdatedAt()
        );
    }
}