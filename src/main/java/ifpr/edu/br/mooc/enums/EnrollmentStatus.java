package ifpr.edu.br.mooc.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnrollmentStatus {
    IN_PROGRESS("in_progress", "Em Progresso"),
    COMPLETED("completed", "Concluído"),
    ALL("all", "Todos");

    private final String code;
    private final String description;

    public static EnrollmentStatus fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return ALL;
        }
        
        for (EnrollmentStatus status : EnrollmentStatus.values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        
        return ALL;
    }
}