package ifpr.edu.br.mooc.dto.campus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CampusReqDto(
        @NotBlank
        @Size(min = 2, max = 255)
        String name,

        @NotNull
        boolean visible
){
}
