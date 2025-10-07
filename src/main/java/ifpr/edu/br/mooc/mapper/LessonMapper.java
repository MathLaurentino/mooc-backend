package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.lesson.AulaAtualizarReqDto;
import ifpr.edu.br.mooc.dto.lesson.AulaCriarReqDto;
import ifpr.edu.br.mooc.dto.lesson.AulaDetalheResDto;
import ifpr.edu.br.mooc.dto.lesson.AulaListaResDto;
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
    Lesson toLesson(AulaCriarReqDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "lessonOrder", ignore = true)
    @Mapping(target = "curso_id", ignore = true)
    void updateLesson(@MappingTarget Lesson lesson, AulaAtualizarReqDto dto);

    @Mapping(target = "curso.id", source = "course.id")
    @Mapping(target = "curso.nome", source = "course.name")
    AulaDetalheResDto toAulaDetalheResDto(Lesson lesson);

    @Mapping(target = "nomeCurso", source = "course.name")
    AulaListaResDto toAulaListaResDto(Lesson lesson);
}