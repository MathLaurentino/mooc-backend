package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.campus.CampusReqDto;
import ifpr.edu.br.mooc.dto.campus.CampusResDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface CampusController {

    ResponseEntity<CampusResDto> createCampus(
            CampusReqDto dto
    );

    ResponseEntity<Page<CampusResDto>> getCampus(
            String name,
            Boolean visible,
            Integer page,
            Integer size,
            String direction
    );

}
