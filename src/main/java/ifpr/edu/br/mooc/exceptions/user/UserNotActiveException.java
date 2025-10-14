package ifpr.edu.br.mooc.exceptions.user;

import ifpr.edu.br.mooc.exceptions.base.ForbiddenException;

public class UserNotActiveException extends ForbiddenException {
    
    public UserNotActiveException() {
        super("Conta do usuário não está ativa");
    }
    
    public UserNotActiveException(String email) {
        super(String.format("Conta com email '%s' não está ativa", email));
    }
}