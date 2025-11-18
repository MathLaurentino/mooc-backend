package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.course.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final LocalFileStorageService fileStorageService;
    private final CourseMapper mapper;
    private final LessonMapper lessonMapper;

    @Value("${server.base-url:http://localhost:8080}")
    private String baseUrl;

    @Transactional
    public CourseDetailResDto createCourse(CourseCreateReqDto dto) {
        if (!knowledgeAreaRepository.existsByIdAndVisibleTrue(dto.areaConhecimentoId()))
            throw new NotFoundException("Área de conhecimento não encontrada.");

        if (!campusRepository.existsByIdAndVisibleTrue(dto.campusId()))
            throw new NotFoundException("Campus não encontrado.");

        Course course = mapper.toCourse(dto);
        course.setVisible(false);
        course.setThumbnail(null);

        var savedCourse = courseRepository.save(course);

        return mapper.toCourseDetailResDto(savedCourse, null);
    }

    @Transactional
    public CourseThumbnailResDto uploadThumbnail(Long courseId, MultipartFile thumbnail) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        // Deleta thumbnail antiga se existir
        if (course.getThumbnail() != null) {
            fileStorageService.deleteCourseThumbnail(course.getThumbnail());
        }

        // Salva nova thumbnail
        String thumbnailPath = fileStorageService.saveCourseThumbnail(thumbnail, courseId);
        course.setThumbnail(thumbnailPath);
        courseRepository.save(course);

        String thumbnailUrl = generateThumbnailUrl(courseId);

        return new CourseThumbnailResDto(courseId, thumbnailUrl);
    }

    @Transactional
    public CourseDetailResDto updateCourse(Long id, CourseUpdateReqDto dto) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        if (!Objects.equals(course.getKnowledgeAreaId(), dto.areaConhecimentoId()) && !knowledgeAreaRepository.existsByIdAndVisibleTrue(dto.areaConhecimentoId()))
            throw new NotFoundException("Área de conhecimento não encontrada");

        if (!Objects.equals(course.getCampusId(), dto.campusId()) && !campusRepository.existsByIdAndVisibleTrue(dto.campusId()))
            throw new NotFoundException("Campus não encontrado");

        mapper.updateCourse(course, dto);

        var savedCourse = courseRepository.save(course);
        String thumbnailUrl = savedCourse.getThumbnail() != null ? generateThumbnailUrl(id) : null;

        return mapper.toCourseDetailResDto(savedCourse, thumbnailUrl);
    }

    @Transactional
    public CourseDetailResDto updateCourseActiveStatus(Long id, boolean active) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        course.setVisible(active);

        var savedCourse = courseRepository.save(course);
        String thumbnailUrl = savedCourse.getThumbnail() != null ? generateThumbnailUrl(id) : null;

        return mapper.toCourseDetailResDto(savedCourse, thumbnailUrl);
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

        // Gera URL da thumbnail se existir
        String thumbnailUrl = course.getThumbnail() != null ? generateThumbnailUrl(id) : null;

        return mapper.toCourseWithLessonsResDto(course, lessonDtos, enrollmentInfo, thumbnailUrl);
    }

    @Transactional(readOnly = true)
    public PageResponse<CourseListResDto> getCourses(
            CourseSpecification spec,
            Pageable pageable
    ) {
        Page<Course> coursesPage = courseRepository.findAll(spec, pageable);
        Map<Long, Long> enrollmentsByCourseId = getEnrollmentsByCourseId();

        List<CourseListResDto> content = coursesPage.getContent().stream()
                .map(course -> {
                    String thumbnailUrl = course.getThumbnail() != null ? generateThumbnailUrl(course.getId()) : null;
                    return mapper.toCourseListResDto(course, enrollmentsByCourseId, thumbnailUrl);
                })
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

    public Resource getThumbnail(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        if (course.getThumbnail() == null || course.getThumbnail().isBlank()) {
            throw new NotFoundException("Curso não possui thumbnail.");
        }

        return fileStorageService.loadCourseThumbnail(course.getThumbnail());
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

    /**
     * Gera URL da thumbnail com parâmetro de versão para invalidar cache do navegador
     * O parâmetro v é baseado no timestamp de última atualização do curso
     */
    public String generateThumbnailUrl(Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null || course.getUpdatedAt() == null) {
            return String.format("%s/mooc/courses/%d/thumbnail", baseUrl, courseId);
        }

        // Usa o timestamp de atualização para invalidar cache
        // Sempre que o curso for atualizado (incluindo upload de nova thumbnail), a URL muda
        long version = course.getUpdatedAt().toEpochSecond(java.time.ZoneOffset.UTC);
        return String.format("%s/mooc/courses/%d/thumbnail?v=%d", baseUrl, courseId, version);
    }
}