package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.CertificateRequestController;
import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestBatchUpdateDto;
import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestResDto;
import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestUpdateDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import ifpr.edu.br.mooc.repository.specification.CertificateRequestSpecification;
import ifpr.edu.br.mooc.service.CertificateRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificate-requests")
@RequiredArgsConstructor
public class CertificateRequestControllerImpl implements CertificateRequestController {

    private final CertificateRequestService certificateRequestService;

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<CertificateRequestResDto>> getAllRequests(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "createdAt"));

        CertificateRequestStatus requestStatus = status != null
                ? CertificateRequestStatus.fromCode(status)
                : null;

        var spec = new CertificateRequestSpecification(requestStatus);
        var response = certificateRequestService.getAllRequests(spec, pageable);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{requestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CertificateRequestResDto> getRequestById(
            @PathVariable String requestId
    ) {
        var response = certificateRequestService.getRequestById(requestId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/{requestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CertificateRequestResDto> updateRequestStatus(
            @PathVariable String requestId,
            @RequestBody @Valid CertificateRequestUpdateDto dto
    ) {
        var response = certificateRequestService.updateRequestStatus(requestId, dto);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateRequestResDto>> batchUpdateRequestStatus(
            @RequestBody @Valid CertificateRequestBatchUpdateDto dto
    ) {
        var response = certificateRequestService.batchUpdateRequestStatus(dto);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/count/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countPendingRequests() {
        Long count = certificateRequestService.countPendingRequests();
        return ResponseEntity.ok(count);
    }
}