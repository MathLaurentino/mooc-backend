package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    @Query("SELECT e FROM Enrollment e WHERE e.userId = :userId AND e.courseId = :courseId")
    Optional<Enrollment> findByUserIdAndCourseId(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId
    );
}
