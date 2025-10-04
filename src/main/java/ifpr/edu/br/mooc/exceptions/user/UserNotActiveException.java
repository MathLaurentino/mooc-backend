package ifpr.edu.br.mooc.exceptions.user;

import ifpr.edu.br.mooc.exceptions.base.ForbiddenException;

public class UserNotActiveException extends ForbiddenException {
    
    public UserNotActiveException() {
        super("User account is not active");
    }
    
    public UserNotActiveException(String email) {
        super(String.format("User account for '%s' is not active", email));
    }
}