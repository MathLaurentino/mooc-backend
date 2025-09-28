package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaReqDto;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaResDto;
import org.springframework.http.ResponseEntity;

public interface KnowledgeAreaController {

    ResponseEntity<KnowledgeAreaResDto> createKnowledgeArea(KnowledgeAreaReqDto dto);

}
