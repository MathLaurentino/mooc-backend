package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.CourseController;
import ifpr.edu.br.mooc.dto.course.CourseCreateReqDto;
import ifpr.edu.br.mooc.dto.course.CourseDetailResDto;
import ifpr.edu.br.mooc.dto.course.CourseListResDto;
import ifpr.edu.br.mooc.dto.course.CourseUpdateReqDto;
import ifpr.edu.br.mooc.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseControllerImpl implements CourseController {

    private final CourseService service;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDetailResDto> createCourse(
            @RequestBody @Valid CourseCreateReqDto dto
    ) {
        var response = service.createCourse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDetailResDto> updateById(Long id, CourseUpdateReqDto dto) {
        return null;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCourseVisibilityById(Long id, Boolean visible) {
        return null;
    }

    @Override
    public ResponseEntity<CourseDetailResDto> getCourseById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Page<CourseListResDto>> getAllCourse(String name, Boolean visible, Long knowledgeAreaId, Long campusId, Integer page, Integer size, String direction) {
        return null;
    }

}
