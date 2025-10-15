package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    Optional<LessonProgress> findByEnrollmentIdAndLessonId(@Param("enrollmentId") Long enrollmentId, @Param("lessonId") Long lessonId);

}
