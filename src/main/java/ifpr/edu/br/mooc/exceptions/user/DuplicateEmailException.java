package ifpr.edu.br.mooc.exceptions.user;

import ifpr.edu.br.mooc.exceptions.base.ConflictException;

public class DuplicateEmailException extends ConflictException {
    
    public DuplicateEmailException() {
        super("Email já cadastrado  no sistema.");
    }
    
    public DuplicateEmailException(String email) {
        super(String.format("Email '%s' já cadastrado no sistema", email));
    }
}