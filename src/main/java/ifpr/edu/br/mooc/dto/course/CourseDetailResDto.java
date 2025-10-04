package ifpr.edu.br.mooc.dto.course;

import java.time.LocalDateTime;

public record CourseDetailResDto(
        Long id,
        String name,
        String description,
        String professorName,
        String thumbnail,
        Integer workload,
        Boolean visible,
        CampusInfo campus,
        KnowledgeAreaInfo knowledgeArea,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record CampusInfo(Long id, String name) {}
    public record KnowledgeAreaInfo(Long id, String name) {}
}