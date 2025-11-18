package ifpr.edu.br.mooc.repository.specification;

import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.entity.Enrollment;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MyCourseSpecification implements Specification<Enrollment> {

    private final Long userId;
    private final String name;
    private final Boolean completed;
    private final Long knowledgeAreaId;
    private final Long campusId;

    public MyCourseSpecification(Long userId, String name, Boolean completed, Long knowledgeAreaId, Long campusId) {
        this.userId = userId;
        this.name = name;
        this.completed = completed;
        this.knowledgeAreaId = knowledgeAreaId;
        this.campusId = campusId;
    }

    @Override
    public Predicate toPredicate(Root<Enrollment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("userId"), userId));

        Join<Enrollment, Course> courseJoin = root.join("course", JoinType.INNER);

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(courseJoin.get("name")),
                    "%" + name.toLowerCase() + "%"
            ));
        }

        if (completed != null){
            if (completed)
                predicates.add(cb.equal(root.get("completed"), true));
            else
                predicates.add(cb.equal(root.get("completed"), false));
        }

        if (knowledgeAreaId != null) {
            predicates.add(cb.equal(courseJoin.get("knowledgeAreaId"), knowledgeAreaId));
        }

        if (campusId != null) {
            predicates.add(cb.equal(courseJoin.get("campusId"), campusId));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}