package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.course.CourseCreateReqDto;
import ifpr.edu.br.mooc.dto.course.CourseDetailResDto;
import ifpr.edu.br.mooc.dto.course.CourseListResDto;
import ifpr.edu.br.mooc.dto.course.CourseUpdateReqDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface CourseController {

    ResponseEntity<CourseDetailResDto> createCourse(CourseCreateReqDto dto);

    ResponseEntity<CourseDetailResDto> updateById(Long id, CourseUpdateReqDto dto);

    ResponseEntity<Void> updateCourseVisibilityById(Long id, Boolean visible);

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
