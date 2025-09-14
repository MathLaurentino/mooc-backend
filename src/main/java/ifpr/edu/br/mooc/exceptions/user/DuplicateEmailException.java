package ifpr.edu.br.mooc.exceptions.user;

import ifpr.edu.br.mooc.exceptions.base.ConflictException;

public class DuplicateEmailException extends ConflictException {
    
    public DuplicateEmailException() {
        super("Email already registered in the system");
    }
    
    public DuplicateEmailException(String email) {
        super(String.format("Email '%s' is already registered in the system", email));
    }
}