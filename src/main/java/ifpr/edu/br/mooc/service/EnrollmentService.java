package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import ifpr.edu.br.mooc.dto.enrollment.MyCoursesResDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.User;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.exceptions.enrollment.EnrollmentAlreadyExistsException;
import ifpr.edu.br.mooc.exceptions.user.UserNotActiveException;
import ifpr.edu.br.mooc.mapper.EnrollmentMapper;
import ifpr.edu.br.mooc.repository.CourseRepository;
import ifpr.edu.br.mooc.repository.EnrollmentRepository;
import ifpr.edu.br.mooc.repository.UserRepository;
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

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
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

                    String thumbnailUrl = generateThumbnailUrl(enrollment.getCourse());

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

    /**
     * Gera URL da thumbnail com parâmetro de versão para invalidar cache do navegador
     */
    private String generateThumbnailUrl(Course course) {
        if (course == null || course.getThumbnail() == null || course.getThumbnail().isBlank()) {
            return null;
        }

        if (course.getUpdatedAt() == null) {
            return String.format("%s/mooc/courses/%d/thumbnail", baseUrl, course.getId());
        }

        // Usa o timestamp de atualização para invalidar cache
        long version = course.getUpdatedAt().toEpochSecond(java.time.ZoneOffset.UTC);
        return String.format("%s/mooc/courses/%d/thumbnail?v=%d", baseUrl, course.getId(), version);
    }

}