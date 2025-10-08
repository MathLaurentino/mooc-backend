package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaReqDto;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaResDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import org.springframework.http.ResponseEntity;

public interface KnowledgeAreaController {

    ResponseEntity<KnowledgeAreaResDto> createKnowledgeArea(
            KnowledgeAreaReqDto dto
    );

    ResponseEntity<PageResponse<KnowledgeAreaResDto>> getKnowledgeAreas(
            String name,
            Boolean visible,
            Integer page,
            Integer size,
            String direction
    );

}
