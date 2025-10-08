package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT MAX(l.lessonOrder) FROM Lesson l WHERE l.courseId = :courseId")
    Optional<Integer> findMaxLessonOrderByCourseId(@Param("courseId") Long courseId);

    List<Lesson> findByCourseIdOrderByLessonOrderAsc(Long courseId);

}
