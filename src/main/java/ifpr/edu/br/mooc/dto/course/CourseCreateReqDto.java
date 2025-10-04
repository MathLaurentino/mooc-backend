package ifpr.edu.br.mooc.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CourseCreateReqDto(
        @NotBlank
        String name,
        
        @NotBlank
        String description,
        
        @NotNull
        Long knowledgeAreaId,
        
        @NotNull
        Long campusId,
        
        @NotBlank
        String professorName,
        
        @NotNull
        @Positive
        Integer workload
) {}