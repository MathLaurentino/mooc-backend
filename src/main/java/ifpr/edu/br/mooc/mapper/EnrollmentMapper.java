package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.enrollment.EnrollmentDTO;
import ifpr.edu.br.mooc.dto.enrollment.EnrollmentRequestDTO;
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
    @Mapping(target = "userId", source = "usuarioId")
    @Mapping(target = "courseId", source = "cursoId")
    Enrollment toEnrollment(EnrollmentRequestDTO dto);

    @Mapping(target = "usuarioId", source = "userId")
    @Mapping(target = "cursoId", source = "courseId")
    @Mapping(target = "concluido", source = "completed")
    @Mapping(target = "concluidoEm", source = "completedAt")
    @Mapping(target = "criadoEm", source = "createdAt")
    EnrollmentDTO toEnrollmentDTO(Enrollment enrollment);

}