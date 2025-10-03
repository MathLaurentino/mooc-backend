package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.CampusController;
import ifpr.edu.br.mooc.dto.campus.CampusReqDto;
import ifpr.edu.br.mooc.dto.campus.CampusResDto;
import ifpr.edu.br.mooc.repository.specification.CampusSpecification;
import ifpr.edu.br.mooc.service.CampusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campus")
@RequiredArgsConstructor
public class CampusControllerImpl implements CampusController {

    private final CampusService service;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampusResDto> createCampus(
            @RequestBody @Valid CampusReqDto dto
    ) {
        var response = service.createCampus(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<CampusResDto>> getCampus(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "visible", required = false) Boolean visible,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "8") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        var spec = new CampusSpecification(name, visible);

        Page<CampusResDto> result = service.getCampus(spec, pageable);

        return ResponseEntity.ok(result);
    }

}
