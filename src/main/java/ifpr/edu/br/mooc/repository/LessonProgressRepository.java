package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    Optional<LessonProgress> findByEnrollmentIdAndLessonId(@Param("enrollmentId") Long enrollmentId, @Param("lessonId") Long lessonId);

    @Query("SELECT lp FROM LessonProgress lp WHERE lp.enrollmentId = :enrollmentId")
    List<LessonProgress> findByEnrollmentId(@Param("enrollmentId") Long enrollmentId);

    @Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.enrollmentId = :enrollmentId AND lp.completed = true")
    Integer countCompletedByEnrollmentId(@Param("enrollmentId") Long enrollmentId);
}
