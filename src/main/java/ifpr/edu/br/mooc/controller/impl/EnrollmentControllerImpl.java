package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.EnrollmentController;
import ifpr.edu.br.mooc.dto.enrollment.CompletedCoursesResDto;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import ifpr.edu.br.mooc.dto.enrollment.MyCoursesResDto;
import ifpr.edu.br.mooc.dto.lessonProgress.LessonProgressResponseDTO;
import ifpr.edu.br.mooc.dto.lessonProgress.ReqLessonProgressDTO;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import ifpr.edu.br.mooc.exceptions.base.BadRequestException;
import ifpr.edu.br.mooc.repository.specification.CompletedCoursesSpecification;
import ifpr.edu.br.mooc.repository.specification.MyCourseSpecification;
import ifpr.edu.br.mooc.security.CurrentUserService;
import ifpr.edu.br.mooc.service.EnrollmentService;
import ifpr.edu.br.mooc.service.LessonProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentControllerImpl implements EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final LessonProgressService lessonProgressService;
    private final CurrentUserService currentUserService;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentDTO> createEnrollment(
            @RequestBody @Valid EnrollmentRequestDTO dto
    ) {
        Long userId = currentUserService.getCurrentUserId();
        var response = enrollmentService.createEnrollment(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PageResponse<MyCoursesResDto>> getMyCourses(
            @RequestParam(value = "nome", required = false) String name,
            @RequestParam(value = "concluido", required = false) Boolean completed,
            @RequestParam(value = "knowledgeAreaId", required = false) Long knowledgeAreaId,
            @RequestParam(value = "campusId", required = false) Long campusId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "8") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));

        Long userId = currentUserService.getCurrentUserId();
        var spec = new MyCourseSpecification(userId, name, completed, knowledgeAreaId, campusId);

        var response = enrollmentService.getMyCourses(spec, pageable);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/{enrollmentId}/lessons/{lessonId}/progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<LessonProgressResponseDTO> lessonProgress(
            @PathVariable Long enrollmentId,
            @PathVariable Long lessonId,
            @RequestBody ReqLessonProgressDTO dto
    ) {
        var response = lessonProgressService.lessonProgress(enrollmentId, lessonId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Override
    @GetMapping("/courses-with-certificate-status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PageResponse<CompletedCoursesResDto>> getCoursesWithCertificateStatus(
            @RequestParam(value = "nome", required = false) String courseName,
            @RequestParam(value = "knowledgeAreaId", required = false) Long knowledgeAreaId,
            @RequestParam(value = "campusId", required = false) Long campusId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "8") Integer size,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "completedAt"));

        Long userId = currentUserService.getCurrentUserId();

        CertificateRequestStatus certificateStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                certificateStatus = CertificateRequestStatus.fromCode(status);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Status de certificado inválido: " + status);
            }
        }

        var spec = new CompletedCoursesSpecification(
                userId,
                courseName,
                knowledgeAreaId,
                campusId,
                certificateStatus
        );

        var response = enrollmentService.getCoursesWithCertificateStatus(spec, pageable);
        return ResponseEntity.ok(response);
    }

}
