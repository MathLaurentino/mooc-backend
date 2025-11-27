package ifpr.edu.br.mooc.dto.knowledgeArea;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record KnowledgeAreaReqDto(
        @NotBlank(message = "{knowledgeArea.name.notblank}")
        @Size(min = 2, max = 255, message = "{knowledgeArea.name.size}")
        String name,

        @NotNull(message = "{knowledgeArea.visible.notnull}")
        boolean visible
) {}