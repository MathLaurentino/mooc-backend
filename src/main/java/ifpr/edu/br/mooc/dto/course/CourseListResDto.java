package ifpr.edu.br.mooc.dto.course;

public record CourseListResDto(
        Long id,
        String nome,
        String nomeProfessor,
        String miniatura,
        Integer cargaHoraria,
        String nomeCampus,
        String nomeAreaConhecimento
) {}