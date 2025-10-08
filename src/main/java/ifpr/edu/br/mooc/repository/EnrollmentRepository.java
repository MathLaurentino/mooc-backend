package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}
