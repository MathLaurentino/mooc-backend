package ifpr.edu.br.mooc.exceptions.base;

public class UnprocessableEntityException extends RuntimeException {
    
    public UnprocessableEntityException(String message) {
        super(message);
    }
    
    public UnprocessableEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}