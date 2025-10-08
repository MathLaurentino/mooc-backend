package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.EnrollmentController;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentControllerImpl implements EnrollmentController {

    @Override
    @PostMapping
    public ResponseEntity<EnrollmentDTO> createEnrollment(EnrollmentRequestDTO dto) {
        return null;
    }

    @Override
    @GetMapping
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(Long id) {
        return null;
    }

}
