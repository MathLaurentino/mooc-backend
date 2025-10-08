package ifpr.edu.br.mooc.dto.course;

import ifpr.edu.br.mooc.dto.lesson.LessonListResDto;
import java.time.LocalDateTime;
import java.util.List;

public record CourseWithLessonsResDto(
        Long id,
        String nome,
        String descricao,
        String nomeProfessor,
        String miniatura,
        Integer cargaHoraria,
        Boolean visivel,
        LocalDateTime criadoEm,
        LocalDateTime editadoEm,
        CampusDto campus,
        AreaConhecimentoDto areaConhecimento,
        List<LessonListResDto> aulas
) {
    public record CampusDto(Long id, String nome) {}
    public record AreaConhecimentoDto(Long id, String nome) {}
}