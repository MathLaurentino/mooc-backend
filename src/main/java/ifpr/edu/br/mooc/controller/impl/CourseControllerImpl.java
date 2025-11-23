package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.CourseController;
import ifpr.edu.br.mooc.dto.course.*;
import ifpr.edu.br.mooc.dto.lesson.*;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.repository.specification.CourseSpecification;
import ifpr.edu.br.mooc.security.CurrentUserService;
import ifpr.edu.br.mooc.service.CourseService;
import ifpr.edu.br.mooc.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseControllerImpl implements CourseController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final CurrentUserService currentUserService;

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
    @PostMapping(value = "/{id}/thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseThumbnailResDto> uploadThumbnail(
            @PathVariable Long id,
            @RequestPart("thumbnail") MultipartFile thumbnail
    ) {
        var response = courseService.uploadThumbnail(id, thumbnail);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<Resource> getThumbnail(@PathVariable Long id) {
        Resource resource = courseService.getThumbnail(id);

        // Detecta o tipo de conteúdo baseado na extensão
        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(Paths.get(resource.getURI()));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (Exception e) {
            // Usa o padrão se não conseguir detectar
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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
            @RequestParam(value = "enrolled", required = false) Boolean enrolled,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "8") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        String sortField = switch (sortBy.toLowerCase()) {
            case "nome" -> "name";
            case "popularidade" -> "popularity";
            default -> "id";
        };

        Sort sort = Sort.by(sortDirection, sortField);
        var pageable = PageRequest.of(page, size, sort);

        boolean isAdmin = false;
        Long userId = null;

        try {
            isAdmin = currentUserService.isCurrentUserAdmin();
            userId = currentUserService.getCurrentUserId();
        } catch (Exception e) {
            // Usuário não logado
        }

        var response = courseService.getCourses(
                name,
                visible,
                knowledgeAreaId,
                campusId,
                enrolled,
                isAdmin,
                userId,
                pageable
        );

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

    @Override
    @DeleteMapping("/{courseId}/lessons/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId
    ) {
        lessonService.deleteLesson(courseId, lessonId);
        return ResponseEntity.noContent().build();
    }

}