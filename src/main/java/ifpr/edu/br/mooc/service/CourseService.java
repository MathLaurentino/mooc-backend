package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.course.*;
import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.mapper.CourseMapper;
import ifpr.edu.br.mooc.repository.CampusRepository;
import ifpr.edu.br.mooc.repository.CourseRepository;
import ifpr.edu.br.mooc.repository.KnowledgeAreaRepository;
import ifpr.edu.br.mooc.repository.specification.CourseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final KnowledgeAreaRepository knowledgeAreaRepository;
    private final CampusRepository campusRepository;
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

    public Page<CourseListResDto> getKnowledgeAreas(
            CourseSpecification spec,
            Pageable pageable
    ) {
        Page<Course> coursesPage = courseRepository.findAll(spec, pageable);

        // Converte cada entidade para DTO
        return coursesPage.map(mapper::toCourseListResDto);
    }

}
