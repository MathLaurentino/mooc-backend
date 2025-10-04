package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.course.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

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

}
