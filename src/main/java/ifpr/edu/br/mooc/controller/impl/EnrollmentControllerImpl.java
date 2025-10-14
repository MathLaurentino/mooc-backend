package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.EnrollmentController;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import ifpr.edu.br.mooc.security.CurrentUserService;
import ifpr.edu.br.mooc.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentControllerImpl implements EnrollmentController {

    private final EnrollmentService service;
    private final CurrentUserService currentUserService;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentDTO> createEnrollment(
            @RequestBody @Valid EnrollmentRequestDTO dto
    ) {
        Long userId = currentUserService.getCurrentUserId();
        System.out.println(userId);
        var response = service.createEnrollment(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(Long id) {
        return null;
    }

}
