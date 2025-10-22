package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestResDto;
import ifpr.edu.br.mooc.entity.CertificateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CertificateRequestMapper {

    @Mapping(target = "status", expression = "java(certificateRequest.getStatus().getCode())")
    @Mapping(target = "statusDescription", expression = "java(certificateRequest.getStatus().getDescription())")
    @Mapping(target = "student.id", source = "enrollment.user.id")
    @Mapping(target = "student.fullName", source = "enrollment.user.fullName")
    @Mapping(target = "student.cpf", source = "enrollment.user.cpf")
    @Mapping(target = "student.email", source = "enrollment.user.email")
    @Mapping(target = "course.id", source = "enrollment.course.id")
    @Mapping(target = "course.name", source = "enrollment.course.name")
    @Mapping(target = "course.workload", source = "enrollment.course.workload")
    @Mapping(target = "course.campusName", source = "enrollment.course.campus.name")
    CertificateRequestResDto toDto(CertificateRequest certificateRequest);
}