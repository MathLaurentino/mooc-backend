package ifpr.edu.br.mooc.dto.course;

import java.time.LocalDateTime;

public record CourseDetailResDto(
        Long id,
        String nome,
        String descricao,
        String nomeProfessor,
        String miniatura,
        Integer cargaHoraria,
        Boolean visivel,
        CampusInfo campus,
        AreaConhecimentoInfo areaConhecimento,
        LocalDateTime criadoEm,
        LocalDateTime editadoEm
) {
    public record CampusInfo(Long id, String nome) {}
    public record AreaConhecimentoInfo(Long id, String nome) {}
}