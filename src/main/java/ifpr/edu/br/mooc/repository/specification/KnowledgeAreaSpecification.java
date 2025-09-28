package ifpr.edu.br.mooc.repository.specification;

import ifpr.edu.br.mooc.entity.KnowledgeArea;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


public class KnowledgeAreaSpecification implements Specification<KnowledgeArea> {

    private String name;
    private Boolean visible;

    public KnowledgeAreaSpecification(String name, Boolean visible) {
        this.name = name;
        this.visible = visible;
    }

    @Override
    public Predicate toPredicate(Root<KnowledgeArea> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (visible != null) {
            predicates.add(criteriaBuilder.equal(root.get("visible"), visible));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}