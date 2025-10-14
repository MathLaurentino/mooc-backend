package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper mapper;

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
        enrollment.setId(userId);
        enrollment.setUser(user);
        enrollment.setCourse(course);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return mapper.toEnrollmentDTO(savedEnrollment);
    }

}
