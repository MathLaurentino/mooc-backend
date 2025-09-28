package ifpr.edu.br.mooc.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Simple principal that holds user information from JWT token.
 * This avoids database calls on every request.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserPrincipal implements Serializable {
    
    private Long id;
    private String email;
    private String fullName;
    
    public JwtUserPrincipal(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}