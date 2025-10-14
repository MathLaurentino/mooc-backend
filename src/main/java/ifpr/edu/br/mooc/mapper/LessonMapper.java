package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.lesson.LessonUpdateReqDto;
import ifpr.edu.br.mooc.dto.lesson.LessonCreateReqDto;
import ifpr.edu.br.mooc.dto.lesson.LessonDetailResDto;
import ifpr.edu.br.mooc.dto.lesson.LessonListResDto;
import ifpr.edu.br.mooc.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "lessonOrder", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "title", source = "titulo")
    @Mapping(target = "description", source = "descricao")
    @Mapping(target = "videoUrl", source = "urlVideo")
    Lesson toLesson(LessonCreateReqDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "lessonOrder", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "title", source = "titulo")
    @Mapping(target = "description", source = "descricao")
    @Mapping(target = "videoUrl", source = "urlVideo")
    void updateLesson(@MappingTarget Lesson lesson, LessonUpdateReqDto dto);

    @Mapping(target = "cursoId", source = "courseId")
    @Mapping(target = "titulo", source = "title")
    @Mapping(target = "descricao", source = "description")
    @Mapping(target = "urlVideo", source = "videoUrl")
    @Mapping(target = "ordemAula", source = "lessonOrder")
    @Mapping(target = "curso.id", source = "course.id")
    @Mapping(target = "curso.nome", source = "course.name")
    LessonDetailResDto toLessonDetailResDto(Lesson lesson);

    @Mapping(target = "titulo", source = "title")
    @Mapping(target = "ordemAula", source = "lessonOrder")
    LessonListResDto toLessonListResDto(Lesson lesson);
}