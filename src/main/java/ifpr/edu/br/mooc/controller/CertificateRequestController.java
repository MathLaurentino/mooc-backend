package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestBatchUpdateDto;
import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestResDto;
import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestUpdateDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CertificateRequestController {

    ResponseEntity<PageResponse<CertificateRequestResDto>> getAllRequests(
            String status,
            Integer page,
            Integer size,
            String direction
    );

    ResponseEntity<CertificateRequestResDto> getRequestById(String requestId);

    ResponseEntity<CertificateRequestResDto> updateRequestStatus(
            String requestId,
            CertificateRequestUpdateDto dto
    );

    ResponseEntity<List<CertificateRequestResDto>> batchUpdateRequestStatus(
            CertificateRequestBatchUpdateDto dto
    );

    ResponseEntity<Long> countPendingRequests();
}