package ifpr.edu.br.mooc.dto.lesson;

public record LessonListResDto(
        Long id,
        String titulo,
        String miniatura,
        Integer ordemAula,
        String nomeCurso
) {}