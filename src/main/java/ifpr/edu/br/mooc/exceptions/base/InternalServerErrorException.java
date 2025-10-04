package ifpr.edu.br.mooc.exceptions.base;

public class InternalServerErrorException extends RuntimeException {
    
    public InternalServerErrorException(String message) {
        super(message);
    }
    
    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}