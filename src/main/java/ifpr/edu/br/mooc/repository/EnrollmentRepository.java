package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>, JpaSpecificationExecutor<Enrollment> {

    /**
     * Verifica se já existe uma matrícula para o usuário e curso especificados
     * @param userId ID do usuário
     * @param courseId ID do curso
     * @return true se existe, false caso contrário
     */
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    List<Enrollment> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"course", "course.campus", "course.knowledgeArea"})
    Page<Enrollment> findAll(Specification<Enrollment> spec, Pageable pageable);
}
