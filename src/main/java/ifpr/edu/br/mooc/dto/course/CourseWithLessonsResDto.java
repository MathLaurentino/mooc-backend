package ifpr.edu.br.mooc.dto.course;

import ifpr.edu.br.mooc.dto.lesson.LessonListResDto;
import java.time.LocalDateTime;
import java.util.List;

public record CourseWithLessonsResDto(
        Long id,
        String name,
        String description,
        String professorName,
        String thumbnail,
        Integer workload,
        Boolean visible,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        CampusDto campus,
        KnowledgeAreaDto knowledgeArea,
        List<LessonListResDto> lessons
) {
    public record CampusDto(Long id, String name) {}
    public record KnowledgeAreaDto(Long id, String name) {}
}