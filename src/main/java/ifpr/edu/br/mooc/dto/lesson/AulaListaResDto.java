package ifpr.edu.br.mooc.dto.lesson;

public record AulaListaResDto(
        Long id,
        String titulo,
        String miniatura,
        Integer ordemAula,
        String nomeCurso
) {}