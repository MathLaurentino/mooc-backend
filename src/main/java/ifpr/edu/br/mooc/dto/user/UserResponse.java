package ifpr.edu.br.mooc.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String fullName,
        String cpf,
        LocalDate birthDate,
        String email,
        Boolean active,
        LocalDateTime createdAt
) {}