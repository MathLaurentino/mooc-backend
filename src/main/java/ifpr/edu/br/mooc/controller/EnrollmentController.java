package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import ifpr.edu.br.mooc.dto.lessonProgress.LessonProgressResponseDTO;
import ifpr.edu.br.mooc.dto.lessonProgress.ReqLessonProgressDTO;
import org.springframework.http.ResponseEntity;

public interface EnrollmentController {

    ResponseEntity<EnrollmentDTO> createEnrollment(EnrollmentRequestDTO dto);

    ResponseEntity<LessonProgressResponseDTO> lessonProgress(
            Long enrollmentId,
            Long lessonId,
            ReqLessonProgressDTO dto
    );

    ResponseEntity<EnrollmentDTO> getEnrollmentById(Long id);

}
