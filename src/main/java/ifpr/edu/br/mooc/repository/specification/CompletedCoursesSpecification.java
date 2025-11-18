package ifpr.edu.br.mooc.repository.specification;

import ifpr.edu.br.mooc.entity.CertificateRequest;
import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CompletedCoursesSpecification implements Specification<Enrollment> {

    private final Long userId;
    private final String courseName;
    private final Long knowledgeAreaId;
    private final Long campusId;
    private final CertificateRequestStatus certificateStatus;

    public CompletedCoursesSpecification(
            Long userId,
            String courseName,
            Long knowledgeAreaId,
            Long campusId,
            CertificateRequestStatus certificateStatus
    ) {
        this.userId = userId;
        this.courseName = courseName;
        this.knowledgeAreaId = knowledgeAreaId;
        this.campusId = campusId;
        this.certificateStatus = certificateStatus;
    }

    @Override
    public Predicate toPredicate(Root<Enrollment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Filtro obrigatório: apenas matrículas do usuário
        predicates.add(cb.equal(root.get("userId"), userId));

        // Filtro obrigatório: apenas cursos concluídos
        predicates.add(cb.equal(root.get("completed"), true));

        // Join com Course
        Join<Enrollment, Course> courseJoin = root.join("course", JoinType.INNER);

        // Filtro de nome do curso
        if (courseName != null && !courseName.isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(courseJoin.get("name")),
                    "%" + courseName.toLowerCase() + "%"
            ));
        }

        // Filtro de área de conhecimento
        if (knowledgeAreaId != null) {
            predicates.add(cb.equal(courseJoin.get("knowledgeAreaId"), knowledgeAreaId));
        }

        // Filtro de campus
        if (campusId != null) {
            predicates.add(cb.equal(courseJoin.get("campusId"), campusId));
        }

        // Filtro de status do certificado
        if (certificateStatus != null) {
            Join<Enrollment, CertificateRequest> certificateJoin = root.join("certificateRequest", JoinType.LEFT);
            predicates.add(cb.equal(certificateJoin.get("status"), certificateStatus));
        }

        // Evitar duplicatas em caso de joins
        query.distinct(true);

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}