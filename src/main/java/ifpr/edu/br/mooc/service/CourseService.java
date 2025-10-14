package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.course.*;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.mapper.CourseMapper;
import ifpr.edu.br.mooc.repository.CampusRepository;
import ifpr.edu.br.mooc.repository.CourseRepository;
import ifpr.edu.br.mooc.repository.EnrollmentRepository;
import ifpr.edu.br.mooc.repository.KnowledgeAreaRepository;
import ifpr.edu.br.mooc.repository.specification.CourseSpecification;
import ifpr.edu.br.mooc.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CurrentUserService currentUserService;
    private final CourseRepository courseRepository;
    private final KnowledgeAreaRepository knowledgeAreaRepository;
    private final CampusRepository campusRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper mapper;

    public CourseDetailResDto createCourse(CourseCreateReqDto dto) {
        if (!knowledgeAreaRepository.existsByIdAndVisibleTrue(dto.areaConhecimentoId()))
            throw new NotFoundException("Área de conhecimento não encontrada.");

        if (!campusRepository.existsByIdAndVisibleTrue(dto.campusId()))
            throw new NotFoundException("Campus não encontrado.");

        Course course = mapper.toCourse(dto);
        course.setVisible(false);

        var savedCourse = courseRepository.save(course);

        return mapper.toCourseDetailResDto(savedCourse);
    }

    public CourseDetailResDto updateCourse(Long id, CourseUpdateReqDto dto) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        if (!Objects.equals(course.getKnowledgeAreaId(), dto.areaConhecimentoId()) && !knowledgeAreaRepository.existsByIdAndVisibleTrue(dto.areaConhecimentoId()))
            throw new NotFoundException("Área de conhecimento não encontrada");

        if (!Objects.equals(course.getCampusId(), dto.campusId()) && !campusRepository.existsByIdAndVisibleTrue(dto.campusId()))
            throw new NotFoundException("Campus não encontrado");

        mapper.updateCourse(course, dto);

        var savedCourse = courseRepository.save(course);

        return mapper.toCourseDetailResDto(savedCourse);
    }

    public CourseDetailResDto updateCourseActiveStatus(Long id, boolean active) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        course.setVisible(active);

        var savedCourse = courseRepository.save(course);

        return mapper.toCourseDetailResDto(savedCourse);
    }

    public CourseWithLessonsResDto getByIdWithLessons(Long id) {
        Course course = courseRepository.findByIdWithLessons(id).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        return mapper.toCourseWithLessonsResDto(course);
    }

    @Transactional(readOnly = true)
    public PageResponse<CourseListResDto> getCourses(
            CourseSpecification spec,
            Pageable pageable
    ) {
        Page<Course> coursesPage = courseRepository.findAll(spec, pageable);
        Set<Long> enrolledCourseIds = getEnrolledCourseIds();

        System.out.println(enrolledCourseIds);

        List<CourseListResDto> content = coursesPage.getContent().stream()
                .map(course -> mapper.toCourseListResDto(course, enrolledCourseIds))
                .toList();

        return new PageResponse<>(
                content,
                coursesPage.getNumber(),
                coursesPage.getSize(),
                coursesPage.getTotalElements(),
                coursesPage.getTotalPages(),
                coursesPage.isFirst(),
                coursesPage.isLast()
        );
    }

    private Set<Long> getEnrolledCourseIds() {
        try {
            Long userId = currentUserService.getCurrentUserId();
            List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
            System.out.println(enrollments);
            return enrollments.stream()
                    .map(Enrollment::getCourseId)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            return Set.of();
        }
    }

}
