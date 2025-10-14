package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Verifica se já existe uma matrícula para o usuário e curso especificados
     * @param userId ID do usuário
     * @param courseId ID do curso
     * @return true se existe, false caso contrário
     */
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    List<Enrollment> findByUserId(Long userId);
}
