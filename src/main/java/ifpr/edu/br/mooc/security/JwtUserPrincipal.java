package ifpr.edu.br.mooc.security;

import ifpr.edu.br.mooc.entity.enums.UserRole;
import lombok.*;

import java.io.Serializable;

/**
 * Simple principal that holds user information from JWT token.
 * This avoids database calls on every request.
 */
@Data
@AllArgsConstructor
@ToString
public class JwtUserPrincipal implements Serializable {
    
    private Long id;
    private String email;
    private String fullName;
    private UserRole role;
    
    public JwtUserPrincipal(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}