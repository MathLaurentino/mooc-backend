package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestBatchUpdateDto;
import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestResDto;
import ifpr.edu.br.mooc.dto.certificateRequest.CertificateRequestUpdateDto;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.entity.CertificateRequest;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import ifpr.edu.br.mooc.exceptions.base.BadRequestException;
import ifpr.edu.br.mooc.exceptions.base.ConflictException;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.mapper.CertificateRequestMapper;
import ifpr.edu.br.mooc.repository.CertificateRequestRepository;
import ifpr.edu.br.mooc.repository.EnrollmentRepository;
import ifpr.edu.br.mooc.repository.specification.CertificateRequestSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateRequestService {

    private final CertificateRequestRepository certificateRequestRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CertificateRequestMapper mapper;
    private final AutoApproveConfigService autoApproveConfigService;

    @Transactional
    public CertificateRequestResDto createCertificateRequest(Long enrollmentId) {
        log.info("Creating certificate request for enrollment: {}", enrollmentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NotFoundException("Inscrição não encontrada"));

        if (!enrollment.getCompleted()) {
            throw new BadRequestException("Não é possível solicitar certificado. O curso ainda não foi concluído.");
        }

        if (certificateRequestRepository.existsByEnrollmentId(enrollmentId)) {
            throw new ConflictException("Já existe uma solicitação de certificado para esta inscrição.");
        }

        // Verifica se o modo de aprovação automática está ativo
        boolean autoApproveEnabled = autoApproveConfigService.isAutoApproveEnabled();
        CertificateRequestStatus initialStatus = autoApproveEnabled
                ? CertificateRequestStatus.APPROVED
                : CertificateRequestStatus.ANALYSIS;

        CertificateRequest request = CertificateRequest.builder()
                .enrollmentId(enrollmentId)
                .enrollment(enrollment)
                .status(initialStatus)
                .build();

        CertificateRequest savedRequest = certificateRequestRepository.save(request);

        if (autoApproveEnabled) {
            log.info("Certificate request created with AUTO-APPROVED status (auto-approve mode is ENABLED) - Request ID: {}",
                    savedRequest.getId());
        } else {
            log.info("Certificate request created with ANALYSIS status (auto-approve mode is DISABLED) - Request ID: {}",
                    savedRequest.getId());
        }

        return mapper.toDto(savedRequest);
    }

    @Transactional(readOnly = true)
    public PageResponse<CertificateRequestResDto> getAllRequests(
            CertificateRequestSpecification spec,
            Pageable pageable
    ) {
        log.info("Fetching certificate requests with filters");

        Page<CertificateRequest> requestsPage = certificateRequestRepository.findAll(
                (Specification<CertificateRequest>) spec,
                pageable
        );

        Page<CertificateRequestResDto> dtoPage = requestsPage.map(mapper::toDto);

        return new PageResponse<>(dtoPage);
    }

    @Transactional(readOnly = true)
    public CertificateRequestResDto getRequestById(String requestId) {
        log.info("Fetching certificate request with id: {}", requestId);

        CertificateRequest request = certificateRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Solicitação de certificado não encontrada"));

        return mapper.toDto(request);
    }

    @Transactional
    public CertificateRequestResDto updateRequestStatus(String requestId, CertificateRequestUpdateDto dto) {
        log.info("Updating certificate request status. ID: {}, New status: {}", requestId, dto.status());

        CertificateRequest request = certificateRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Solicitação de certificado não encontrada"));

        if (request.getStatus() != CertificateRequestStatus.ANALYSIS) {
            throw new BadRequestException(
                    String.format("Não é possível alterar o status. A solicitação já foi %s.",
                            request.getStatus().getDescription().toLowerCase())
            );
        }

        CertificateRequestStatus newStatus = CertificateRequestStatus.fromCode(dto.status());
        request.setStatus(newStatus);

        if (newStatus == CertificateRequestStatus.REJECTED) {
            request.setRejectionReason(dto.motivoReprovacao());
        }

        CertificateRequest updatedRequest = certificateRequestRepository.save(request);

        log.info("Certificate request status updated successfully. ID: {}, Status: {}",
                requestId, newStatus);

        return mapper.toDto(updatedRequest);
    }

    @Transactional
    public List<CertificateRequestResDto> batchUpdateRequestStatus(CertificateRequestBatchUpdateDto dto) {
        log.info("Batch updating {} certificate requests with status: {}",
                dto.requests().size(), dto.status());

        List<CertificateRequestResDto> results = new ArrayList<>();

        CertificateRequestUpdateDto updateDto = new CertificateRequestUpdateDto(
                dto.status(),
                null
        );

        for (CertificateRequestBatchUpdateDto.RequestUpdate requestUpdate : dto.requests()) {
            try {
                CertificateRequestResDto result = updateRequestStatus(
                        requestUpdate.requestId(),
                        updateDto
                );

                results.add(result);
            } catch (Exception e) {
                log.error("Error updating certificate request {}: {}",
                        requestUpdate.requestId(), e.getMessage());
                throw e;
            }
        }

        log.info("Batch update completed successfully. {} requests updated", results.size());

        return results;
    }

    @Transactional(readOnly = true)
    public long countPendingRequests() {
        return certificateRequestRepository.countByStatus(CertificateRequestStatus.ANALYSIS);
    }

    @Transactional
    public void autoApproveAllPendingRequests() {
        log.info("Auto-approving all pending certificate requests");

        List<CertificateRequest> pendingRequests = certificateRequestRepository
                .findByStatus(CertificateRequestStatus.ANALYSIS);

        if (pendingRequests.isEmpty()) {
            log.info("No pending requests to auto-approve");
            return;
        }

        pendingRequests.forEach(request -> {
            request.setStatus(CertificateRequestStatus.APPROVED);
            log.debug("Auto-approving certificate request: {}", request.getId());
        });

        certificateRequestRepository.saveAll(pendingRequests);

        log.info("Auto-approved {} certificate requests", pendingRequests.size());
    }
}