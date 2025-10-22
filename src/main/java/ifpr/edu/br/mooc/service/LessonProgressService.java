package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.lessonProgress.LessonProgressResponseDTO;
import ifpr.edu.br.mooc.dto.lessonProgress.ReqLessonProgressDTO;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.Lesson;
import ifpr.edu.br.mooc.entity.LessonProgress;
import ifpr.edu.br.mooc.exceptions.base.ConflictException;
import ifpr.edu.br.mooc.exceptions.base.UnauthorizedException;
import ifpr.edu.br.mooc.repository.EnrollmentRepository;
import ifpr.edu.br.mooc.repository.LessonProgressRepository;
import ifpr.edu.br.mooc.repository.LessonRepository;
import ifpr.edu.br.mooc.security.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LessonProgressService {

    private final CurrentUserService currentUserService;
    private final LessonProgressRepository lessonProgressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final CertificateRequestService certificateRequestService;

    @Transactional
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

        // Verifica se todas as aulas foram concluídas e atualiza o enrollment
        if (dto.concluido() && !enrollment.getCompleted()) {
            boolean allCompleted = enrollmentRepository.isEnrollmentCompleted(enrollmentId);

            if (allCompleted) {
                enrollment.setCompleted(true);
                enrollment.setCompletedAt(LocalDateTime.now());
                enrollmentRepository.save(enrollment);

                log.info("Enrollment {} completed. Creating certificate request...", enrollmentId);

                // Cria automaticamente a solicitação de certificado
                try {
                    certificateRequestService.createCertificateRequest(enrollmentId);
                    log.info("Certificate request created successfully for enrollment {}", enrollmentId);
                } catch (ConflictException e) {
                    // Se já existe uma solicitação, apenas loga (não é um erro crítico)
                    log.warn("Certificate request already exists for enrollment {}", enrollmentId);
                } catch (Exception e) {
                    // Loga o erro mas não falha a operação de progresso
                    log.error("Error creating certificate request for enrollment {}: {}",
                            enrollmentId, e.getMessage(), e);
                }
            }
        }

        return LessonProgressResponseDTO.fromEntity(saved);
    }
}