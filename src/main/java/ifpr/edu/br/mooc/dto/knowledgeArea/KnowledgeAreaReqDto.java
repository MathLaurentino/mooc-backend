package ifpr.edu.br.mooc.dto.knowledgeArea;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record KnowledgeAreaReqDto(
        @NotBlank
        @Size(min = 2, max = 255)
        String name,

        @NotNull
        Boolean active
){
}
