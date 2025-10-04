package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaReqDto;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaResDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface KnowledgeAreaController {

    ResponseEntity<KnowledgeAreaResDto> createKnowledgeArea(
            KnowledgeAreaReqDto dto
    );

    ResponseEntity<Page<KnowledgeAreaResDto>> getKnowledgeAreas(
            String name,
            Boolean visible,
            Integer page,
            Integer size,
            String direction
    );

}
