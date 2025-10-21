package ifpr.edu.br.mooc.dto.course;

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
        List<LessonListResDto> aulas,
        InscricaoInfoDto inscricaoInfo
) {
    public record CampusDto(Long id, String nome) {}
    public record AreaConhecimentoDto(Long id, String nome) {}

    public record InscricaoInfoDto(
            Long inscricaoId,
            Boolean estaInscrito,
            Boolean concluido,
            LocalDateTime inscritoEm,
            LocalDateTime concluidoEm,
            Integer totalAulas,
            Integer aulasConcluidas
    ) {}

    public record LessonListResDto(
            Long id,
            String titulo,
            Integer ordemAula,
            Boolean concluido
    ) {}

}