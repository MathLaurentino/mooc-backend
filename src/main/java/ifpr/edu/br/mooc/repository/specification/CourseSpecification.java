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
    private final boolean isAdmin;
    private final Long userId;

    public CourseSpecification(String name, Boolean visible, Long knowledgeAreaId, Long campusId, boolean isAdmin, Long userId) {
        this.name = name;
        this.visible = visible;
        this.knowledgeAreaId = knowledgeAreaId;
        this.campusId = campusId;
        this.isAdmin = isAdmin;
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (isAdmin) {
            if (visible != null) {
                predicates.add(cb.equal(root.get("visible"), visible));
            }
        } else {
            if (visible != null && visible) {
                predicates.add(cb.equal(root.get("visible"), true));
            } else if (visible != null && !visible) {
                // Requisição: visible=false
                // Só admin pode listar cursos invisíveis propositalmente
                // Para alunos: retorna apenas cursos invisíveis onde está inscrito

                if (userId != null) {
                    // Aluno logado pedindo invisíveis - mostrar apenas onde está inscrito
                    Subquery<Long> enrolledCoursesSubquery = query.subquery(Long.class);
                    Root<Enrollment> enrollmentRoot = enrolledCoursesSubquery.from(Enrollment.class);
                    enrolledCoursesSubquery.select(enrollmentRoot.get("courseId"));
                    enrolledCoursesSubquery.where(cb.equal(enrollmentRoot.get("userId"), userId));

                    predicates.add(cb.and(
                            cb.equal(root.get("visible"), false),
                            root.get("id").in(enrolledCoursesSubquery)
                    ));
                } else {
                    // Usuário não logado pedindo invisíveis - retorna vazio
                    predicates.add(cb.equal(root.get("visible"), true));
                    predicates.add(cb.equal(root.get("visible"), false)); // Contradição intencional = vazio
                }

            } else {
                // Requisição: visible=null (não especificado)
                // Mostrar visíveis + invisíveis onde está inscrito (se logado)

                if (userId != null) {
                    // Usuário logado - mostrar visíveis + invisíveis onde está inscrito
                    Subquery<Long> enrolledCoursesSubquery = query.subquery(Long.class);
                    Root<Enrollment> enrollmentRoot = enrolledCoursesSubquery.from(Enrollment.class);
                    enrolledCoursesSubquery.select(enrollmentRoot.get("courseId"));
                    enrolledCoursesSubquery.where(cb.equal(enrollmentRoot.get("userId"), userId));

                    predicates.add(cb.or(
                            cb.equal(root.get("visible"), true),
                            root.get("id").in(enrolledCoursesSubquery)
                    ));
                } else {
                    // Usuário não logado - mostrar apenas visíveis
                    predicates.add(cb.equal(root.get("visible"), true));
                }
            }
        }

        if (knowledgeAreaId != null) {
            predicates.add(cb.equal(root.get("knowledgeAreaId"), knowledgeAreaId));
        }

        if (campusId != null) {
            predicates.add(cb.equal(root.get("campusId"), campusId));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}