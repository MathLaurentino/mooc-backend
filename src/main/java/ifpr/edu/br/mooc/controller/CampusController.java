package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.campus.CampusReqDto;
import ifpr.edu.br.mooc.dto.campus.CampusResDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import org.springframework.http.ResponseEntity;

public interface CampusController {

    ResponseEntity<CampusResDto> createCampus(
            CampusReqDto dto
    );

    ResponseEntity<PageResponse<CampusResDto>> getCampus(
            String name,
            Boolean visible,
            Integer page,
            Integer size,
            String direction
    );

}
