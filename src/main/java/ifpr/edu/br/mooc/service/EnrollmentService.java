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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper mapper;

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

        Page<MyCoursesResDto> dtoPage = enrollmentPage.map(mapper::toMyCoursesResDto);

        return new PageResponse<>(dtoPage);
    }

}
