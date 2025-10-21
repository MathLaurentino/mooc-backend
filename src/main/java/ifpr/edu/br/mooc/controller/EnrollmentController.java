package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import ifpr.edu.br.mooc.dto.enrollment.MyCoursesResDto;
import ifpr.edu.br.mooc.dto.lessonProgress.LessonProgressResponseDTO;
import ifpr.edu.br.mooc.dto.lessonProgress.ReqLessonProgressDTO;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import org.springframework.http.ResponseEntity;

public interface EnrollmentController {

    ResponseEntity<EnrollmentDTO> createEnrollment(EnrollmentRequestDTO dto);

    ResponseEntity<PageResponse<MyCoursesResDto>> getMyCourses(
            String name,
            Boolean completed,
            Integer page,
            Integer size,
            String direction
    );

    ResponseEntity<LessonProgressResponseDTO> lessonProgress(
            Long enrollmentId,
            Long lessonId,
            ReqLessonProgressDTO dto
    );

}
