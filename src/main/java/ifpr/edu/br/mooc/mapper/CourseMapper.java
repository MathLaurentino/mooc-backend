package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.course.CourseCreateReqDto;
import ifpr.edu.br.mooc.dto.course.CourseDetailResDto;
import ifpr.edu.br.mooc.dto.course.CourseListResDto;
import ifpr.edu.br.mooc.dto.course.CourseUpdateReqDto;
import ifpr.edu.br.mooc.entity.Campus;
import ifpr.edu.br.mooc.entity.Course;
import ifpr.edu.br.mooc.entity.KnowledgeArea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "knowledgeArea", source = "knowledgeArea")
    @Mapping(target = "campus", source = "campus")
    Course toCourse(CourseCreateReqDto dto, KnowledgeArea knowledgeArea, Campus campus);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "knowledgeArea", source = "knowledgeArea")
    @Mapping(target = "campus", source = "campus")
    void updateCourse(@MappingTarget Course course, CourseUpdateReqDto dto, KnowledgeArea knowledgeArea, Campus campus);

    @Mapping(target = "campus.id", source = "campus.id")
    @Mapping(target = "campus.name", source = "campus.name")
    @Mapping(target = "knowledgeArea.id", source = "knowledgeArea.id")
    @Mapping(target = "knowledgeArea.name", source = "knowledgeArea.name")
    CourseDetailResDto toCourseDetailResDto(Course course);

    @Mapping(target = "campusName", source = "campus.name")
    @Mapping(target = "knowledgeAreaName", source = "knowledgeArea.name")
    CourseListResDto toCourseListResDto(Course course);
}