package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.campus.CampusReqDto;
import ifpr.edu.br.mooc.dto.campus.CampusResDto;
import ifpr.edu.br.mooc.entity.Campus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CampusMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Campus toCampus(CampusReqDto dto);

    CampusResDto toCampusResDto(Campus knowledgeArea);
}
