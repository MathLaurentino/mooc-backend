package ifpr.edu.br.mooc.dto.enrollment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {

    private Long id;
    private Long usuarioId;
    private Long cursoId;
    private Boolean concluido;
    private LocalDateTime concluidoEm;
    private LocalDateTime criadoEm;
}