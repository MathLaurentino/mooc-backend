package ifpr.edu.br.mooc.dto.lesson;

public record LessonListResDto(
        Long id,
        String titulo,
        Integer ordemAula
) {}