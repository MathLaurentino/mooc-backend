package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaReqDto;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaResDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.entity.KnowledgeArea;
import ifpr.edu.br.mooc.exceptions.knowledgeArea.DuplicatedKnowledgeAreaNameException;
import ifpr.edu.br.mooc.mapper.KnowledgeAreaMapper;
import ifpr.edu.br.mooc.repository.KnowledgeAreaRepository;
import ifpr.edu.br.mooc.repository.specification.KnowledgeAreaSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KnowledgeAreaService {

    private final KnowledgeAreaRepository repository;
    private final KnowledgeAreaMapper mapper;

    public KnowledgeAreaResDto createKnowledgeArea(KnowledgeAreaReqDto dto) {
        if (repository.existsByName(dto.name())){
            throw new DuplicatedKnowledgeAreaNameException(dto.name());
        }

        var createdKnowledgeArea = repository.save(mapper.toKnowledgeArea(dto));

        return mapper.toKnowledgeAreaResDto(createdKnowledgeArea);
    }

    public PageResponse<KnowledgeAreaResDto> getKnowledgeAreas(
            KnowledgeAreaSpecification spec,
            Pageable pageable
    ) {
        Page<KnowledgeArea> knowledgeAreasPage = repository.findAll(spec, pageable);

        // Converte cada entidade para DTO
        return new PageResponse<>(knowledgeAreasPage.map(mapper::toKnowledgeAreaResDto));
    }


}
