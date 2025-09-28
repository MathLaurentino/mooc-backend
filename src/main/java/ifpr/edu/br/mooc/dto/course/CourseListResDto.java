package ifpr.edu.br.mooc.dto.course;

public record CourseListResDto(
        Long id,
        String name,
        String professorName,
        String thumbnail,
        Integer workload,
        String campusName,
        String knowledgeAreaName
) {}