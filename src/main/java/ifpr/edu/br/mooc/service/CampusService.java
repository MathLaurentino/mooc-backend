package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.campus.CampusReqDto;
import ifpr.edu.br.mooc.dto.campus.CampusResDto;
import ifpr.edu.br.mooc.entity.Campus;
import ifpr.edu.br.mooc.exceptions.knowledgeArea.DuplicatedKnowledgeAreaNameException;
import ifpr.edu.br.mooc.mapper.CampusMapper;
import ifpr.edu.br.mooc.repository.CampusRepository;
import ifpr.edu.br.mooc.repository.specification.CampusSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampusService {

    private final CampusRepository repository;
    private final CampusMapper mapper;

    public CampusResDto createCampus(CampusReqDto dto) {
        if (repository.existsByName(dto.name())){
            throw new DuplicatedKnowledgeAreaNameException(dto.name());
        }

        var createdCampus = repository.save(mapper.toCampus(dto));

        return mapper.toCampusResDto(createdCampus);
    }

    public Page<CampusResDto> getCampus(
            CampusSpecification spec,
            Pageable pageable
    ) {
        Page<Campus> knowledgeAreasPage = repository.findAll(spec, pageable);

        return knowledgeAreasPage.map(mapper::toCampusResDto);
    }

}
