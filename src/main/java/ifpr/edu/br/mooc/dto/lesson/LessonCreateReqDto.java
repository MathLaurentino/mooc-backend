package ifpr.edu.br.mooc.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LessonCreateReqDto(
        @NotNull(message = "ID do curso é obrigatório")
        Long cursoId,
        
        @NotBlank(message = "Título é obrigatório")
        @Size(min=5, max = 100, message = "Título deve ter no máximo 100 caracteres")
        String titulo,
        
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,
        
        @NotBlank(message = "URL do vídeo é obrigatória")
        String urlVideo
) {}
