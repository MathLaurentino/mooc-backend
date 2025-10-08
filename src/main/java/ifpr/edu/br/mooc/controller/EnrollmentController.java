package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import org.springframework.http.ResponseEntity;

public interface EnrollmentController {

    ResponseEntity<EnrollmentDTO> createEnrollment(EnrollmentRequestDTO dto);

    ResponseEntity<EnrollmentDTO> getEnrollmentById(Long id);

}
