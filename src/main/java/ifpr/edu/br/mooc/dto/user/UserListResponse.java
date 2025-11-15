package ifpr.edu.br.mooc.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserListResponse(
        Long id,
        String fullName,
        String email,
        String cpf,
        LocalDate birthDate,
        Boolean active,
        LocalDateTime createdAt
) {
}