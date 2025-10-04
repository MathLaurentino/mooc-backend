package ifpr.edu.br.mooc.mapper;

import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaReqDto;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaResDto;
import ifpr.edu.br.mooc.entity.KnowledgeArea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface KnowledgeAreaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    KnowledgeArea toKnowledgeArea(KnowledgeAreaReqDto dto);

    KnowledgeAreaResDto toKnowledgeAreaResDto(KnowledgeArea knowledgeArea);
}
