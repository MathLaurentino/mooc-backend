package ifpr.edu.br.mooc.exceptions.user;

import ifpr.edu.br.mooc.exceptions.base.ConflictException;

public class DuplicateCpfException extends ConflictException {
    
    public DuplicateCpfException() {
        super("CPF already registered in the system");
    }
    
    public DuplicateCpfException(String cpf) {
        super(String.format("CPF '%s' is already registered in the system", maskCpf(cpf)));
    }
    
    private static String maskCpf(String cpf) {
        if (cpf == null || cpf.length() < 11) {
            return "***";
        }
        return cpf.substring(0, 3) + ".***.**-" + cpf.substring(9);
    }
}