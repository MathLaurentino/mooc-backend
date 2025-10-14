package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.course.*;
import ifpr.edu.br.mooc.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = LessonMapper.class
)
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "knowledgeArea", ignore = true)
    @Mapping(target = "campus", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "visible", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "name", source = "nome")
    @Mapping(target = "description", source = "descricao")
    @Mapping(target = "knowledgeAreaId", source = "areaConhecimentoId")
    @Mapping(target = "campusId", source = "campusId")
    @Mapping(target = "professorName", source = "nomeProfessor")
    @Mapping(target = "workload", source = "cargaHoraria")
    Course toCourse(CourseCreateReqDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "knowledgeArea", ignore = true)
    @Mapping(target = "campus", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "name", source = "nome")
    @Mapping(target = "description", source = "descricao")
    @Mapping(target = "knowledgeAreaId", source = "areaConhecimentoId")
    @Mapping(target = "campusId", source = "campusId")
    @Mapping(target = "professorName", source = "nomeProfessor")
    @Mapping(target = "workload", source = "cargaHoraria")
    @Mapping(target = "visible", source = "visivel")
    void updateCourse(@MappingTarget Course course, CourseUpdateReqDto dto);

    @Mapping(target = "nome", source = "name")
    @Mapping(target = "descricao", source = "description")
    @Mapping(target = "nomeProfessor", source = "professorName")
    @Mapping(target = "miniatura", source = "thumbnail")
    @Mapping(target = "cargaHoraria", source = "workload")
    @Mapping(target = "visivel", source = "visible")
    @Mapping(target = "campus.id", source = "campus.id")
    @Mapping(target = "campus.nome", source = "campus.name")
    @Mapping(target = "areaConhecimento.id", source = "knowledgeArea.id")
    @Mapping(target = "areaConhecimento.nome", source = "knowledgeArea.name")
    @Mapping(target = "criadoEm", source = "createdAt")
    @Mapping(target = "editadoEm", source = "updatedAt")
    CourseDetailResDto toCourseDetailResDto(Course course);

    @Mapping(target = "nome", source = "name")
    @Mapping(target = "nomeProfessor", source = "professorName")
    @Mapping(target = "miniatura", source = "thumbnail")
    @Mapping(target = "cargaHoraria", source = "workload")
    @Mapping(target = "campus.id", source = "campus.id")
    @Mapping(target = "campus.nome", source = "campus.name")
    @Mapping(target = "areaConhecimento.id", source = "knowledgeArea.id")
    @Mapping(target = "areaConhecimento.nome", source = "knowledgeArea.name")
    @Mapping(target = "isEnrolled", ignore = true) // ✅ Ignora no mapeamento automático
    CourseListResDto toCourseListResDto(Course course);

    @Mapping(target = "nome", source = "course.name")
    @Mapping(target = "nomeProfessor", source = "course.professorName")
    @Mapping(target = "miniatura", source = "course.thumbnail")
    @Mapping(target = "cargaHoraria", source = "course.workload")
    @Mapping(target = "campus.id", source = "course.campus.id")
    @Mapping(target = "campus.nome", source = "course.campus.name")
    @Mapping(target = "areaConhecimento.id", source = "course.knowledgeArea.id")
    @Mapping(target = "areaConhecimento.nome", source = "course.knowledgeArea.name")
    @Mapping(target = "isEnrolled", expression = "java(enrolledCourseIds.contains(course.getId()))")
    CourseListResDto toCourseListResDto(Course course, Set<Long> enrolledCourseIds);

    @Mapping(target = "nome", source = "name")
    @Mapping(target = "descricao", source = "description")
    @Mapping(target = "nomeProfessor", source = "professorName")
    @Mapping(target = "miniatura", source = "thumbnail")
    @Mapping(target = "cargaHoraria", source = "workload")
    @Mapping(target = "visivel", source = "visible")
    @Mapping(target = "criadoEm", source = "createdAt")
    @Mapping(target = "editadoEm", source = "updatedAt")
    @Mapping(target = "campus.id", source = "campus.id")
    @Mapping(target = "campus.nome", source = "campus.name")
    @Mapping(target = "areaConhecimento.id", source = "knowledgeArea.id")
    @Mapping(target = "areaConhecimento.nome", source = "knowledgeArea.name")
    @Mapping(target = "aulas", source = "lessons")
    CourseWithLessonsResDto toCourseWithLessonsResDto(Course course);
}