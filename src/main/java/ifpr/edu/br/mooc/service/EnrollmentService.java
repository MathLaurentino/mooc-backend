package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.User;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
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

    public EnrollmentDTO createEnrollment(EnrollmentRequestDTO dto) {
        Course course = courseRepository.findById(dto.cursoId()).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        if (!course.getVisible())
            throw new NotFoundException("Curso não encontrado.");

        User user = userRepository.findById(dto.usuarioId()).orElseThrow(
                () -> new NotFoundException("Usuário não encontrado."));

        if (!user.getActive())
            throw new NotFoundException("Usuário não encontrado.");

        Enrollment enrollment = mapper.toEnrollment(dto);
        enrollment.setCourse(course);
        enrollment.setUser(user);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return mapper.toEnrollmentDTO(savedEnrollment);
    }

}
