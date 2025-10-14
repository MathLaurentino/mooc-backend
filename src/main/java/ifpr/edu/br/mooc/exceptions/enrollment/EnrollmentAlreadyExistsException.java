package ifpr.edu.br.mooc.exceptions.enrollment;

import ifpr.edu.br.mooc.exceptions.base.ConflictException;

public class EnrollmentAlreadyExistsException extends ConflictException {

    public EnrollmentAlreadyExistsException(){
        super("Matrícula já registrada no sistema.");
    }

    public EnrollmentAlreadyExistsException(String email, String cursoName){
        super(String.format("Conta com email '%s' já possui matricula no curso '%s'.", cursoName, email));
    }

}
