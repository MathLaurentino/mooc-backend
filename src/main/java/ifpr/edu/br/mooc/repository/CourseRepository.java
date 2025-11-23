package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.id = :id")
    Optional<Course> findByIdWithLessons(@Param("id") Long id);

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.courseId = :courseId")
    Integer countLessonsByCourseId(@Param("courseId") Long courseId);

    @Query(value = """
    SELECT DISTINCT c.*, COALESCE(enrollment_count.count, 0) as popularity
    FROM curso c
    LEFT JOIN (
        SELECT curso_id, COUNT(*) as count
        FROM inscricao
        GROUP BY curso_id
    ) enrollment_count ON c.id = enrollment_count.curso_id
    LEFT JOIN inscricao e ON c.id = e.curso_id AND e.usuario_id = :userId
    WHERE 1=1
        AND (:name IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', CAST(:name AS TEXT), '%')))
        AND (
            :isAdmin = true 
            OR (
                :visible IS NULL AND (
                    c.visivel = true 
                    OR (:userId IS NOT NULL AND e.id IS NOT NULL)
                )
            )
            OR (:visible = true AND c.visivel = true)
            OR (:visible = false AND :userId IS NOT NULL AND c.visivel = false AND e.id IS NOT NULL)
        )
        AND (:knowledgeAreaId IS NULL OR c.area_conhecimento_id = :knowledgeAreaId)
        AND (:campusId IS NULL OR c.campus_id = :campusId)
        AND (
            :enrolled IS NULL 
            OR (:enrolled = true AND :userId IS NOT NULL AND e.id IS NOT NULL)
            OR (:enrolled = false AND :userId IS NOT NULL AND e.id IS NULL)
        )
    ORDER BY popularity DESC
    """,
            countQuery = """
    SELECT COUNT(DISTINCT c.id)
    FROM curso c
    LEFT JOIN inscricao e ON c.id = e.curso_id AND e.usuario_id = :userId
    WHERE 1=1
        AND (:name IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', CAST(:name AS TEXT), '%')))
        AND (
            :isAdmin = true 
            OR (
                :visible IS NULL AND (
                    c.visivel = true 
                    OR (:userId IS NOT NULL AND e.id IS NOT NULL)
                )
            )
            OR (:visible = true AND c.visivel = true)
            OR (:visible = false AND :userId IS NOT NULL AND c.visivel = false AND e.id IS NOT NULL)
        )
        AND (:knowledgeAreaId IS NULL OR c.area_conhecimento_id = :knowledgeAreaId)
        AND (:campusId IS NULL OR c.campus_id = :campusId)
        AND (
            :enrolled IS NULL 
            OR (:enrolled = true AND :userId IS NOT NULL AND e.id IS NOT NULL)
            OR (:enrolled = false AND :userId IS NOT NULL AND e.id IS NULL)
        )
    """,
            nativeQuery = true)
    Page<Course> findAllByPopularity(
            @Param("name") String name,
            @Param("visible") Boolean visible,
            @Param("knowledgeAreaId") Long knowledgeAreaId,
            @Param("campusId") Long campusId,
            @Param("enrolled") Boolean enrolled,
            @Param("userId") Long userId,
            @Param("isAdmin") boolean isAdmin,
            Pageable pageable
    );
}