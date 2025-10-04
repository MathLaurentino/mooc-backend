package ifpr.edu.br.mooc.repository.specification;

import ifpr.edu.br.mooc.entity.Course;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CourseSpecification implements Specification<Course> {

    private final String name;
    private final Boolean visible;
    private final Long knowledgeAreaId;
    private final Long campusId;

    public CourseSpecification(String name, Boolean visible, Long knowledgeAreaId, Long campusId) {
        this.name = name;
        this.visible = visible;
        this.knowledgeAreaId = knowledgeAreaId;
        this.campusId = campusId;
    }

    @Override
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (visible != null) {
            predicates.add(criteriaBuilder.equal(root.get("visible"), visible));
        }

        if (knowledgeAreaId != null) {
            predicates.add(criteriaBuilder.equal(root.get("knowledgeAreaId"), knowledgeAreaId));
        }

        if (campusId != null) {
            predicates.add(criteriaBuilder.equal(root.get("campusId"), campusId));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}