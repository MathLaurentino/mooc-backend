package ifpr.edu.br.mooc.dto.campus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CampusReqDto(
        @NotBlank(message = "{campus.name.notblank}")
        @Size(min = 2, max = 255, message = "{campus.name.size}")
        String name,

        @NotNull(message = "{campus.visible.notnull}")
        boolean visible
) {}