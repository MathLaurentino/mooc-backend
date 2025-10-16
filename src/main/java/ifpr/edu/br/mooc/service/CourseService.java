package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.course.*;
import ifpr.edu.br.mooc.dto.lesson.LessonListResDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.Lesson;
import ifpr.edu.br.mooc.entity.LessonProgress;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.mapper.CourseMapper;
import ifpr.edu.br.mooc.mapper.LessonMapper;
import ifpr.edu.br.mooc.repository.*;
import ifpr.edu.br.mooc.repository.specification.CourseSpecification;
import ifpr.edu.br.mooc.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CurrentUserService currentUserService;
    private final CourseRepository courseRepository;
    private final KnowledgeAreaRepository knowledgeAreaRepository;
    private final CampusRepository campusRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final CourseMapper mapper;
    private final LessonMapper lessonMapper;

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

    @Transactional(readOnly = true)
    public CourseWithLessonsResDto getByIdWithLessons(Long id) {
        Course course = courseRepository.findByIdWithLessons(id).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        // Buscar informações de inscrição (se o usuário estiver logado)
        CourseWithLessonsResDto.InscricaoInfoDto enrollmentInfo = getEnrollmentInfo(id);

        // Buscar progresso das aulas (se houver inscrição)
        Map<Long, Boolean> completedLessonsMap = new HashMap<>();
        if (enrollmentInfo != null && enrollmentInfo.inscricaoId() != null) {
            List<LessonProgress> progressList = lessonProgressRepository
                    .findByEnrollmentId(enrollmentInfo.inscricaoId());
            completedLessonsMap = progressList.stream()
                    .collect(Collectors.toMap(
                            LessonProgress::getLessonId,
                            LessonProgress::getCompleted,
                            (existing, replacement) -> replacement
                    ));
        }

        Map<Long, Boolean> finalCompletedMap = completedLessonsMap;
        List<CourseWithLessonsResDto.LessonListResDto> lessonDtos = course.getLessons().stream()
                .sorted(Comparator.comparing(Lesson::getLessonOrder))
                .map(lesson -> new CourseWithLessonsResDto.LessonListResDto(
                        lesson.getId(),
                        lesson.getTitle(),
                        lesson.getLessonOrder(),
                        finalCompletedMap.getOrDefault(lesson.getId(), false)
                ))
                .toList();

        return mapper.toCourseWithLessonsResDto(course, lessonDtos, enrollmentInfo);
    }

    @Transactional(readOnly = true)
    public PageResponse<CourseListResDto> getCourses(
            CourseSpecification spec,
            Pageable pageable
    ) {
        Page<Course> coursesPage = courseRepository.findAll(spec, pageable);
        Map<Long, Long> enrollmentsByCourseId = getEnrollmentsByCourseId();

        List<CourseListResDto> content = coursesPage.getContent().stream()
                .map(course -> mapper.toCourseListResDto(course, enrollmentsByCourseId))
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

    private Map<Long, Long> getEnrollmentsByCourseId() {
        try {
            Long userId = currentUserService.getCurrentUserId();
            List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
            return enrollments.stream()
                    .collect(Collectors.toMap(
                            Enrollment::getCourseId,
                            Enrollment::getId,
                            (existing, replacement) -> existing
                    ));
        } catch (Exception e) {
            return Map.of();
        }
    }

    private CourseWithLessonsResDto.InscricaoInfoDto getEnrollmentInfo(Long courseId) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            Optional<Enrollment> enrollmentOpt = enrollmentRepository
                    .findByUserIdAndCourseId(userId, courseId);

            if (enrollmentOpt.isEmpty()) {
                return null;
            }

            Enrollment enrollment = enrollmentOpt.get();
            Integer totalLessons = courseRepository.countLessonsByCourseId(courseId);
            Integer completedLessons = lessonProgressRepository
                    .countCompletedByEnrollmentId(enrollment.getId());

            return new CourseWithLessonsResDto.InscricaoInfoDto(
                    enrollment.getId(),
                    true,
                    enrollment.getCompleted(),
                    enrollment.getCreatedAt(),
                    enrollment.getCompletedAt(),
                    totalLessons,
                    completedLessons
            );
        } catch (Exception e) {
            // Usuário não logado ou erro ao buscar
            return null;
        }
    }
}