package ifpr.edu.br.mooc.exceptions.user;

import ifpr.edu.br.mooc.exceptions.base.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {
    
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}