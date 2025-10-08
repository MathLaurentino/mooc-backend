package ifpr.edu.br.mooc.dto.lesson;

import java.time.LocalDateTime;

public record LessonDetailResDto(
        Long id,
        Long cursoId,
        String titulo,
        String descricao,
        String miniatura,
        String urlVideo,
        Integer ordemAula,
        CursoInfo curso
) {
    public record CursoInfo(Long id, String nome) {}
}