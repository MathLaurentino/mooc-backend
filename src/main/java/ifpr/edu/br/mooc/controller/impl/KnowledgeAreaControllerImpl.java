package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.KnowledgeAreaController;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaReqDto;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaResDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.repository.specification.KnowledgeAreaSpecification;
import ifpr.edu.br.mooc.service.KnowledgeAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/knowledge-area")
@RequiredArgsConstructor
public class KnowledgeAreaControllerImpl implements KnowledgeAreaController {

    private final KnowledgeAreaService service;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KnowledgeAreaResDto> createKnowledgeArea(
            @RequestBody @Valid KnowledgeAreaReqDto dto
    ) {
        var response = service.createKnowledgeArea(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<KnowledgeAreaResDto>> getKnowledgeAreas(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "active", required = false) Boolean visible,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "8") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        var spec = new KnowledgeAreaSpecification(name, visible);

        PageResponse<KnowledgeAreaResDto> result = service.getKnowledgeAreas(spec, pageable);

        return ResponseEntity.ok(result);
    }
}
