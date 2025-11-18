package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.enrollment.CompletedCoursesResDto;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import ifpr.edu.br.mooc.dto.enrollment.MyCoursesResDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.entity.CertificateRequest;
import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.User;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.exceptions.enrollment.EnrollmentAlreadyExistsException;
import ifpr.edu.br.mooc.exceptions.user.UserNotActiveException;
import ifpr.edu.br.mooc.mapper.EnrollmentMapper;
import ifpr.edu.br.mooc.repository.CertificateRequestRepository;
import ifpr.edu.br.mooc.repository.CourseRepository;
import ifpr.edu.br.mooc.repository.EnrollmentRepository;
import ifpr.edu.br.mooc.repository.UserRepository;
import ifpr.edu.br.mooc.repository.specification.CompletedCoursesSpecification;
import ifpr.edu.br.mooc.repository.specification.MyCourseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final CourseService courseService;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CertificateRequestRepository certificateRequestRepository;
    private final EnrollmentMapper mapper;

    @Value("${server.base-url:http://localhost:8080}")
    private String baseUrl;

    @Transactional
    public EnrollmentDTO createEnrollment(EnrollmentRequestDTO dto, Long userId) {
        Course course = courseRepository.findById(dto.cursoId()).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        if (!course.getVisible())
            throw new NotFoundException("Curso não encontrado.");

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Usuário não encontrado."));

        if (!user.getActive())
            throw new UserNotActiveException();

        if (enrollmentRepository.existsByUserIdAndCourseId(userId, dto.cursoId()))
            throw new EnrollmentAlreadyExistsException();

        Enrollment enrollment = mapper.toEnrollment(dto);
        enrollment.setUserId(userId);
        enrollment.setUser(user);
        enrollment.setCourse(course);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return mapper.toEnrollmentDTO(savedEnrollment);
    }

    @Transactional(readOnly = true)
    public PageResponse<MyCoursesResDto> getMyCourses(MyCourseSpecification spec, Pageable pageable) {
        Page<Enrollment> enrollmentPage = enrollmentRepository.findAll(spec, pageable);

        // Mapeia os enrollments e adiciona a URL completa da thumbnail
        List<MyCoursesResDto> dtoList = enrollmentPage.getContent().stream()
                .map(enrollment -> {
                    MyCoursesResDto dto = mapper.toMyCoursesResDto(enrollment);

                    String thumbnailUrl = courseService.generateThumbnailUrl(enrollment.getCourse().getId());

                    return new MyCoursesResDto(
                            dto.enrollmentId(),
                            dto.cursoId(),
                            dto.nome(),
                            dto.nomeProfessor(),
                            thumbnailUrl,
                            dto.cargaHoraria(),
                            dto.concluido(),
                            dto.campus(),
                            dto.areaConhecimento()
                    );
                })
                .collect(Collectors.toList());

        return new PageResponse<>(
                dtoList,
                enrollmentPage.getNumber(),
                enrollmentPage.getSize(),
                enrollmentPage.getTotalElements(),
                enrollmentPage.getTotalPages(),
                enrollmentPage.isFirst(),
                enrollmentPage.isLast()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<CompletedCoursesResDto> getCoursesWithCertificateStatus(
            CompletedCoursesSpecification spec,
            Pageable pageable
    ) {
        Page<Enrollment> enrollments = enrollmentRepository.findAll(spec, pageable);

        Page<CompletedCoursesResDto> completedCoursesPage = enrollments.map(enrollment -> {
            // Busca a solicitação de certificado
            CertificateRequest certificateRequest = certificateRequestRepository
                    .findByEnrollmentId(enrollment.getId())
                    .orElse(null);

            // Extrai informações do certificado
            String certificateStatus = certificateRequest != null
                    ? certificateRequest.getStatus().getCode()
                    : null;
            String certificateStatusDescription = certificateRequest != null
                    ? certificateRequest.getStatus().getDescription()
                    : null;
            String certificateRequestId = certificateRequest != null
                    ? certificateRequest.getId()
                    : null;

            // Gera URL da thumbnail
            String thumbnailUrl = enrollment.getCourse().getThumbnail() != null
                    ? courseService.generateThumbnailUrl(enrollment.getCourse().getId())
                    : null;

            return mapper.toCompletedCoursesResDto(
                    enrollment,
                    certificateStatus,
                    certificateStatusDescription,
                    certificateRequestId,
                    thumbnailUrl
            );
        });

        return new PageResponse<>(completedCoursesPage);
    }

}