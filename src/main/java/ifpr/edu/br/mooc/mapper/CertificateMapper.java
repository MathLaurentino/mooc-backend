package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.certificate.CertificateResponseDto;
import ifpr.edu.br.mooc.entity.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CertificateMapper {

    @Mapping(target = "certificateUrl", ignore = true)
    CertificateResponseDto toDto(Certificate certificate);
}