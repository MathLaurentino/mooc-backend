package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
import ifpr.edu.br.mooc.dto.enrollment.MyCoursesResDto;
import ifpr.edu.br.mooc.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnrollmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "completed", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "courseId", source = "cursoId")
    Enrollment toEnrollment(EnrollmentRequestDTO dto);

    @Mapping(target = "usuarioId", source = "userId")
    @Mapping(target = "cursoId", source = "courseId")
    @Mapping(target = "concluido", source = "completed")
    @Mapping(target = "concluidoEm", source = "completedAt")
    @Mapping(target = "criadoEm", source = "createdAt")
    EnrollmentDTO toEnrollmentDTO(Enrollment enrollment);

    @Mapping(target = "enrollmentId", source = "id")
    @Mapping(target = "cursoId", source = "course.id")
    @Mapping(target = "nome", source = "course.name")
    @Mapping(target = "nomeProfessor", source = "course.professorName")
    @Mapping(target = "miniatura", source = "course.thumbnail")
    @Mapping(target = "cargaHoraria", source = "course.workload")
    @Mapping(target = "concluido", source = "completed")
    @Mapping(target = "campus.id", source = "course.campus.id")
    @Mapping(target = "campus.nome", source = "course.campus.name")
    @Mapping(target = "areaConhecimento.id", source = "course.knowledgeArea.id")
    @Mapping(target = "areaConhecimento.nome", source = "course.knowledgeArea.name")
    MyCoursesResDto toMyCoursesResDto(Enrollment enrollment);

}