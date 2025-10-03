package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.CourseController;
import ifpr.edu.br.mooc.dto.course.*;
import ifpr.edu.br.mooc.repository.specification.CourseSpecification;
import ifpr.edu.br.mooc.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDetailResDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid CourseUpdateReqDto dto
    ) {
        var response = service.updateCourse(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDetailResDto> updateCourseVisibilityById(
            @PathVariable Long id,
            @RequestBody @Valid CoursePatchVisibleDto dto
    ) {
        var response = service.updateCourseActiveStatus(id, dto.visible());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailResDto> getCourseById(
            @PathVariable Long id
    ) {
        var response = service.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<CourseListResDto>> getAllCourse(
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

        var response = service.getKnowledgeAreas(spec, pageable);
        return ResponseEntity.ok(response);
    }

}
