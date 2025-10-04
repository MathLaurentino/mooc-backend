package ifpr.edu.br.mooc.exceptions.knowledgeArea;

import ifpr.edu.br.mooc.exceptions.base.ConflictException;

public class DuplicatedKnowledgeAreaNameException extends ConflictException {

    public DuplicatedKnowledgeAreaNameException() {
        super("Área de conhecimento já cadastrada.");
    }

    public DuplicatedKnowledgeAreaNameException(String name) {
        super(String.format("Área de conhecimento com nome '%s' já cadastrada.", name));
    }

}
