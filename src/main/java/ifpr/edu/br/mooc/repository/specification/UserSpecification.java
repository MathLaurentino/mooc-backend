package ifpr.edu.br.mooc.repository.specification;

import ifpr.edu.br.mooc.entity.User;
import ifpr.edu.br.mooc.entity.enums.UserRole;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {

    private final String name;
    private final String email;
    private final Boolean active;

    public UserSpecification(String name, String email, Boolean active) {
        this.name = name;
        this.email = email;
        this.active = active;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // Filtro para retornar apenas ALUNOS
        predicates.add(criteriaBuilder.equal(root.get("userRole"), UserRole.STUDENT));

        // Filtro por nome (busca parcial, case insensitive)
        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("fullName")),
                    "%" + name.toLowerCase() + "%"
            ));
        }

        // Filtro por email (busca parcial, case insensitive)
        if (email != null && !email.isEmpty()) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%"
            ));
        }

        // Filtro por status ativo/inativo
        if (active != null) {
            predicates.add(criteriaBuilder.equal(root.get("active"), active));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}