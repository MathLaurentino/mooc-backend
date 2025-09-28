package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaReqDto;
import ifpr.edu.br.mooc.dto.knowledgeArea.KnowledgeAreaResDto;
import ifpr.edu.br.mooc.exceptions.knowledgeArea.DuplicatedKnowledgeAreaNameException;
import ifpr.edu.br.mooc.mapper.KnowledgeAreaMapper;
import ifpr.edu.br.mooc.repository.KnowledgeAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
