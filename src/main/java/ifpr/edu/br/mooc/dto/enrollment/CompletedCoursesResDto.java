package ifpr.edu.br.mooc.dto.enrollment;

public record CompletedCoursesResDto(
        Long enrollmentId,
        Long cursoId,
        String nome,
        String minuatura,
        Integer cargaHoraria,
        String statusCertificado,
        String statusCertificadoDescricao,
        String soliciftacaoCertificadoId,

        MyCoursesResDto.CampusDto campus,
        MyCoursesResDto.AreaConhecimentoDto areaConhecimento
) {

    public record CampusDto(Long id, String nome) {}
    public record AreaConhecimentoDto(Long id, String nome) {}

}