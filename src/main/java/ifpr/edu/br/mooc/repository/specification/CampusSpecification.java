package ifpr.edu.br.mooc.repository.specification;

import ifpr.edu.br.mooc.entity.Campus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CampusSpecification implements Specification<Campus> {

    private String name;
    private Boolean visible;

    public CampusSpecification(String name, Boolean visible) {
        this.name = name;
        this.visible = visible;
    }

    @Override
    public Predicate toPredicate(Root<Campus> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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