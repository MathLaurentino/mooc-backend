package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.course.CourseCreateReqDto;
import ifpr.edu.br.mooc.dto.course.CourseDetailResDto;
import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.mapper.CourseMapper;
import ifpr.edu.br.mooc.repository.CampusRepository;
import ifpr.edu.br.mooc.repository.CourseRepository;
import ifpr.edu.br.mooc.repository.KnowledgeAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final KnowledgeAreaRepository knowledgeAreaRepository;
    private final CampusRepository campusRepository;
    private final CourseMapper mapper;

    public CourseDetailResDto createCourse(CourseCreateReqDto dto) {
        if (!knowledgeAreaRepository.existsByIdAndVisibleTrue(dto.knowledgeAreaId()))
            throw new NotFoundException("Área de conhecimento não encontrada");

        if (!campusRepository.existsByIdAndVisibleTrue(dto.campusId()))
            throw new NotFoundException("Campus não encontrado");

        Course course = mapper.toCourse(dto);
        course.setVisible(false);

        var savedCourse = courseRepository.save(course);

        return mapper.toCourseDetailResDto(savedCourse);
    }

}
