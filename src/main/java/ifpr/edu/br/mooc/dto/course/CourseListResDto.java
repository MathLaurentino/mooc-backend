package ifpr.edu.br.mooc.dto.course;

public record CourseListResDto(
        Long id,
        String nome,
        String nomeProfessor,
        String miniatura,
        Integer cargaHoraria,
        CampusDto campus,
        AreaConhecimentoDto areaConhecimento,
        Long enrollmentId
) {
    public record CampusDto(Long id, String nome) {}
    public record AreaConhecimentoDto(Long id, String nome) {}
}