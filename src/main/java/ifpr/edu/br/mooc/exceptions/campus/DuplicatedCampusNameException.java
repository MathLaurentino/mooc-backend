package ifpr.edu.br.mooc.exceptions.campus;

import ifpr.edu.br.mooc.exceptions.base.ConflictException;

public class DuplicatedCampusNameException extends ConflictException {

    public DuplicatedCampusNameException() {
        super("Campi já cadastrado.");
    }

    public DuplicatedCampusNameException(String name) {
        super(String.format("Campi com nome '%s' já cadastrado.", name));
    }

}
