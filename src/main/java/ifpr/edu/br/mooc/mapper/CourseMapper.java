package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.course.*;
import ifpr.edu.br.mooc.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

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
    Course toCourse(CourseCreateReqDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "knowledgeArea", ignore = true)
    @Mapping(target = "campus", ignore = true)
    void updateCourse(@MappingTarget Course course, CourseUpdateReqDto dto);

    @Mapping(target = "campus.id", source = "campus.id")
    @Mapping(target = "campus.name", source = "campus.name")
    @Mapping(target = "knowledgeArea.id", source = "knowledgeArea.id")
    @Mapping(target = "knowledgeArea.name", source = "knowledgeArea.name")
    CourseDetailResDto toCourseDetailResDto(Course course);

    @Mapping(target = "campusName", source = "campus.name")
    @Mapping(target = "knowledgeAreaName", source = "knowledgeArea.name")
    CourseListResDto toCourseListResDto(Course course);

    @Mapping(target = "campus.id", source = "campus.id")
    @Mapping(target = "campus.name", source = "campus.name")
    @Mapping(target = "knowledgeArea.id", source = "knowledgeArea.id")
    @Mapping(target = "knowledgeArea.name", source = "knowledgeArea.name")
    @Mapping(target = "lessons", source = "lessons")
    CourseWithLessonsResDto toCourseWithLessonsResDto(Course course);
}