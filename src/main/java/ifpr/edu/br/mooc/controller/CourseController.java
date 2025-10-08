package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.course.*;
import ifpr.edu.br.mooc.dto.lesson.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseController {

    ResponseEntity<CourseDetailResDto> createCourse(CourseCreateReqDto dto);

    ResponseEntity<CourseDetailResDto> updateById(Long id, CourseUpdateReqDto dto);

    ResponseEntity<CourseDetailResDto> updateCourseVisibilityById(Long id, CoursePatchVisibleDto visible);

    ResponseEntity<CourseDetailResDto> getCourseById(Long id);

    ResponseEntity<Page<CourseListResDto>> getAllCourse(
            String name,
            Boolean visible,
            Long knowledgeAreaId,
            Long campusId,
            Integer page,
            Integer size,
            String direction
    );

    // ========== LESSON ENDPOINTS ==========

    ResponseEntity<LessonDetailResDto> createLesson(
            Long courseId,
            LessonCreateReqDto dto
    );

    ResponseEntity<List<LessonListResDto>> getLessonsByCourse(Long courseId);

    ResponseEntity<LessonDetailResDto> getLessonById(
            Long courseId,
            Long lessonId
    );

    ResponseEntity<LessonDetailResDto> updateLesson(
            Long courseId,
            Long lessonId,
            LessonUpdateReqDto dto
    );

    ResponseEntity<Void> reorderLessons(
            Long courseId,
            LessonReorderReqDto dto
    );

}