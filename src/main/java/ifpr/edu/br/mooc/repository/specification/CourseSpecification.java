package ifpr.edu.br.mooc.repository.specification;

import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.entity.Enrollment;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@ToString
public class CourseSpecification implements Specification<Course> {

    private final String name;
    private final Boolean visible;
    private final Long knowledgeAreaId;
    private final Long campusId;
    private final Boolean enrolled;
    private final boolean isAdmin;
    private final Long userId;

    public CourseSpecification(String name, Boolean visible, Long knowledgeAreaId, Long campusId,
                               Boolean enrolled, boolean isAdmin, Long userId) {
        this.name = name;
        this.visible = visible;
        this.knowledgeAreaId = knowledgeAreaId;
        this.campusId = campusId;
        this.enrolled = enrolled;
        this.isAdmin = isAdmin;
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        // Lógica de visibilidade
        if (isAdmin) {
            if (visible != null) {
                predicates.add(cb.equal(root.get("visible"), visible));
            }
        } else {
            if (visible != null && visible) {
                predicates.add(cb.equal(root.get("visible"), true));
            } else if (visible != null && !visible) {
                if (userId != null) {
                    Subquery<Long> enrolledCoursesSubquery = query.subquery(Long.class);
                    Root<Enrollment> enrollmentRoot = enrolledCoursesSubquery.from(Enrollment.class);
                    enrolledCoursesSubquery.select(enrollmentRoot.get("courseId"));
                    enrolledCoursesSubquery.where(cb.equal(enrollmentRoot.get("userId"), userId));

                    predicates.add(cb.and(
                            cb.equal(root.get("visible"), false),
                            root.get("id").in(enrolledCoursesSubquery)
                    ));
                } else {
                    predicates.add(cb.equal(root.get("visible"), true));
                    predicates.add(cb.equal(root.get("visible"), false));
                }
            } else {
                if (userId != null) {
                    Subquery<Long> enrolledCoursesSubquery = query.subquery(Long.class);
                    Root<Enrollment> enrollmentRoot = enrolledCoursesSubquery.from(Enrollment.class);
                    enrolledCoursesSubquery.select(enrollmentRoot.get("courseId"));
                    enrolledCoursesSubquery.where(cb.equal(enrollmentRoot.get("userId"), userId));

                    predicates.add(cb.or(
                            cb.equal(root.get("visible"), true),
                            root.get("id").in(enrolledCoursesSubquery)
                    ));
                } else {
                    predicates.add(cb.equal(root.get("visible"), true));
                }
            }
        }

        // Filtro de área de conhecimento
        if (knowledgeAreaId != null) {
            predicates.add(cb.equal(root.get("knowledgeAreaId"), knowledgeAreaId));
        }

        // Filtro de campus
        if (campusId != null) {
            predicates.add(cb.equal(root.get("campusId"), campusId));
        }

        // Filtro de matrícula (enrolled)
        if (enrolled != null && userId != null) {
            Subquery<Long> enrolledCoursesSubquery = query.subquery(Long.class);
            Root<Enrollment> enrollmentRoot = enrolledCoursesSubquery.from(Enrollment.class);
            enrolledCoursesSubquery.select(enrollmentRoot.get("courseId"));
            enrolledCoursesSubquery.where(cb.equal(enrollmentRoot.get("userId"), userId));

            if (enrolled) {
                // Cursos onde está matriculado
                predicates.add(root.get("id").in(enrolledCoursesSubquery));
            } else {
                // Cursos onde NÃO está matriculado
                predicates.add(cb.not(root.get("id").in(enrolledCoursesSubquery)));
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}