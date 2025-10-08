package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.CourseController;
import ifpr.edu.br.mooc.dto.course.*;
import ifpr.edu.br.mooc.dto.lesson.*;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.repository.specification.CourseSpecification;
import ifpr.edu.br.mooc.service.CourseService;
import ifpr.edu.br.mooc.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseControllerImpl implements CourseController {

    private final CourseService courseService;
    private final LessonService lessonService;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDetailResDto> createCourse(
            @RequestBody @Valid CourseCreateReqDto dto
    ) {
        var response = courseService.createCourse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDetailResDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid CourseUpdateReqDto dto
    ) {
        var response = courseService.updateCourse(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDetailResDto> updateCourseVisibilityById(
            @PathVariable Long id,
            @RequestBody @Valid CoursePatchVisibleDto dto
    ) {
        var response = courseService.updateCourseActiveStatus(id, dto.visivel());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CourseWithLessonsResDto> getByIdWithLessons(
            @PathVariable Long id
    ) {
        var response = courseService.getByIdWithLessons(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<CourseListResDto>> getAllCourse(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "visible", required = false) Boolean visible,
            @RequestParam(value = "knowledgeAreaId", required = false) Long knowledgeAreaId,
            @RequestParam(value = "campusId", required = false) Long campusId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "8") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        var spec = new CourseSpecification(name, visible, knowledgeAreaId, campusId);

        var response = courseService.getKnowledgeAreas(spec, pageable);
        return ResponseEntity.ok(response);
    }

    // ========== LESSON ENDPOINTS ==========

    @Override
    @PostMapping("/{courseId}/lessons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LessonDetailResDto> createLesson(
            @PathVariable Long courseId,
            @RequestBody @Valid LessonCreateReqDto dto
    ) {
        var response = lessonService.createLesson(dto, courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<List<LessonListResDto>> getLessonsByCourse(
            @PathVariable Long courseId
    ) {
        var response = lessonService.getLessonByCourse(courseId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/{courseId}/lessons/reorder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reorderLessons(
            @PathVariable Long courseId,
            @RequestBody @Valid LessonReorderReqDto dto
    ) {
        lessonService.reorderLessons(courseId, dto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{courseId}/lessons/{lessonId}")
    public ResponseEntity<LessonDetailResDto> getLessonById(
            @PathVariable Long courseId,
            @PathVariable Long lessonId
    ) {
        var response = lessonService.getLessonById(courseId, lessonId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/{courseId}/lessons/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LessonDetailResDto> updateLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @RequestBody @Valid LessonUpdateReqDto dto
    ) {
        var response = lessonService.updateLesson(dto, courseId, lessonId);
        return ResponseEntity.ok(response);
    }

}