package ifpr.edu.br.mooc.dto.enrollment;

public record MyCoursesResDto(
        Long enrollmentId,
        Long cursoId,
        String nome,
        String nomeProfessor,
        String miniatura,
        Integer cargaHoraria,
        Boolean concluido,

        CampusDto campus,
        AreaConhecimentoDto areaConhecimento
) {
    public record CampusDto(Long id, String nome) {}
    public record AreaConhecimentoDto(Long id, String nome) {}
}