package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.KnowledgeAreaController;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaReqDto;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaResDto;
import ifpr.edu.br.mooc.service.KnowledgeAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/knowledge-area")
@RequiredArgsConstructor
public class KnowledgeAreaControllerImpl implements KnowledgeAreaController {

    private final KnowledgeAreaService service;

    @Override
    @PostMapping
    public ResponseEntity<KnowledgeAreaResDto> createKnowledgeArea(
            @RequestBody @Valid KnowledgeAreaReqDto dto
    ) {
        var response = service.createKnowledgeArea(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
