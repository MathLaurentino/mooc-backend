package ifpr.edu.br.mooc.exceptions;

import ifpr.edu.br.mooc.exceptions.base.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ==================== VALIDATION EXCEPTIONS ====================
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {
        
        log.error("Validation error: {}", ex.getMessage());
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.BAD_REQUEST, 
                "Validation failed", 
                ex.getBindingResult()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    // ==================== 400 BAD REQUEST ====================
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequest(
            BadRequestException ex, 
            HttpServletRequest request) {
        
        log.error("Bad request: {}", ex.getMessage());
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.BAD_REQUEST, 
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    // ==================== 401 UNAUTHORIZED ====================
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorMessage> handleUnauthorized(
            UnauthorizedException ex, 
            HttpServletRequest request) {
        
        log.error("Unauthorized access: {}", ex.getMessage());
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.UNAUTHORIZED, 
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    // ==================== 403 FORBIDDEN ====================
    
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorMessage> handleForbidden(
            ForbiddenException ex, 
            HttpServletRequest request) {
        
        log.error("Forbidden access: {}", ex.getMessage());
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.FORBIDDEN, 
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

    // ==================== 404 NOT FOUND ====================
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(
            NotFoundException ex, 
            HttpServletRequest request) {
        
        log.error("Resource not found: {}", ex.getMessage());
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.NOT_FOUND, 
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorMessage> handleNoResourceFound(
            NoResourceFoundException ex, 
            HttpServletRequest request) {
        
        log.error("No resource found: {}", ex.getMessage());
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.NOT_FOUND, 
                "The requested resource was not found"
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    // ==================== 409 CONFLICT ====================
    
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorMessage> handleConflict(
            ConflictException ex, 
            HttpServletRequest request) {
        
        log.error("Conflict: {}", ex.getMessage());
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.CONFLICT, 
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    // ==================== 422 UNPROCESSABLE ENTITY ====================
    
    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorMessage> handleUnprocessableEntity(
            UnprocessableEntityException ex, 
            HttpServletRequest request) {
        
        log.error("Unprocessable entity: {}", ex.getMessage());
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.UNPROCESSABLE_ENTITY, 
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessage);
    }

    // ==================== 500 INTERNAL SERVER ERROR ====================
    
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorMessage> handleInternalServerError(
            InternalServerErrorException ex, 
            HttpServletRequest request) {
        
        log.error("Internal server error: {}", ex.getMessage(), ex);
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.INTERNAL_SERVER_ERROR, 
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    // ==================== DATABASE EXCEPTIONS ====================
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, 
            HttpServletRequest request) {
        
        log.error("Data integrity violation: {}", ex.getMessage());
        
        String message = "Data integrity violation";
        
        // Check for specific constraint violations
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("usuario_email_key")) {
                message = "Email already exists in the system";
            } else if (ex.getMessage().contains("usuario_cpf_key")) {
                message = "CPF already exists in the system";
            }
        }
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.CONFLICT, 
                message
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    // ==================== HTTP MESSAGE EXCEPTIONS ====================

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.error("Request body is missing or malformed: {}", ex.getMessage());

        String message = "Corpo da requisição é obrigatório e deve conter JSON válido";

        // Personalizar mensagem baseada na causa específica
        if (ex.getMessage().contains("Required request body is missing")) {
            message = "Corpo da requisição é obrigatório para este endpoint";
        } else if (ex.getMessage().contains("JSON parse error")) {
            message = "Formato JSON inválido no corpo da requisição";
        }

        ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.BAD_REQUEST, message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    // ==================== GENERIC EXCEPTION ====================
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(
            Exception ex, 
            HttpServletRequest request) {
        
        log.error("Unexpected error occurred", ex);
        
        ErrorMessage errorMessage = new ErrorMessage(
                request, 
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "An unexpected error occurred. Please try again later."
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}