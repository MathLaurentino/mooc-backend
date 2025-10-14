package ifpr.edu.br.mooc.exceptions.user;

import ifpr.edu.br.mooc.exceptions.base.ConflictException;

public class DuplicateCpfException extends ConflictException {
    
    public DuplicateCpfException() {
        super("CPF já registrado no sistema");
    }
    
    public DuplicateCpfException(String cpf) {
        super(String.format("CPF '%s' já registrado no sistema", maskCpf(cpf)));
    }
    
    private static String maskCpf(String cpf) {
        if (cpf == null || cpf.length() < 11) {
            return "***";
        }
        return cpf.substring(0, 3) + ".***.**-" + cpf.substring(9);
    }
}