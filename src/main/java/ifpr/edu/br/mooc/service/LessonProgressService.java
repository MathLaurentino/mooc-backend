package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.lessonProgress.LessonProgressResponseDTO;
import ifpr.edu.br.mooc.dto.lessonProgress.ReqLessonProgressDTO;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.Lesson;
import ifpr.edu.br.mooc.entity.LessonProgress;
import ifpr.edu.br.mooc.exceptions.base.UnauthorizedException;
import ifpr.edu.br.mooc.repository.EnrollmentRepository;
import ifpr.edu.br.mooc.repository.LessonProgressRepository;
import ifpr.edu.br.mooc.repository.LessonRepository;
import ifpr.edu.br.mooc.security.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LessonProgressService {

    private final CurrentUserService currentUserService;
    private final LessonProgressRepository lessonProgressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;

    public LessonProgressResponseDTO lessonProgress(
            Long enrollmentId,
            Long lessonId,
            ReqLessonProgressDTO dto
    ) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Inscrição não encontrada"));

        if(!Objects.equals(enrollment.getUserId(), currentUserService.getCurrentUserId()))
            throw new UnauthorizedException("Id de inscrição não pertence ao usuario autenticado.");

        LessonProgress lessonProgress = lessonProgressRepository
                .findByEnrollmentIdAndLessonId(enrollmentId, lessonId)
                .orElseGet(() -> {
                    Lesson lesson = lessonRepository.findById(lessonId)
                            .orElseThrow(() -> new EntityNotFoundException("Aula não encontrada"));

                    LessonProgress newProgress = new LessonProgress();
                    newProgress.setEnrollmentId(enrollment.getId());
                    newProgress.setEnrollment(enrollment);
                    newProgress.setLessonId(lesson.getId());
                    newProgress.setLesson(lesson);

                    return newProgress;
                });

        lessonProgress.setCompleted(dto.concluido());

        LessonProgress saved = lessonProgressRepository.save(lessonProgress);
        return LessonProgressResponseDTO.fromEntity(saved);
    }
}
