package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.CertificateController;
import ifpr.edu.br.mooc.dto.certificate.CertificateValidationResponseDto;
import ifpr.edu.br.mooc.dto.certificate.GenerateCertificateRequestDto;
import ifpr.edu.br.mooc.dto.certificate.ValidateCertificateByCodeRequestDto;
import ifpr.edu.br.mooc.service.CertificateService;
import ifpr.edu.br.mooc.service.CertificateValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@Slf4j
public class CertificateControllerImpl implements CertificateController {

    private final CertificateService certificateService;
    private final CertificateValidationService validationService;

    /**
     * Endpoint único que gera ou retorna PDF do certificado
     * - Verifica se já existe certificado
     * - Compara hash dos dados
     * - Cria novo registro se dados mudaram
     * - Retorna PDF do certificado
     */
    @Override
    @PostMapping("/generate")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<byte[]> generateOrDownloadCertificate(
            @RequestBody @Valid GenerateCertificateRequestDto dto
    ) {
        log.info("Received request to generate/download certificate for enrollment: {}", dto.enrollmentId());

        byte[] pdfBytes = certificateService.generateOrGetCertificatePdf(dto.enrollmentId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificado.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * Valida certificado pelo código (UUID)
     * Endpoint público - não requer autenticação
     */
    @Override
    @PostMapping("/validate/code")
    public ResponseEntity<CertificateValidationResponseDto> validateByCode(
            @RequestBody @Valid ValidateCertificateByCodeRequestDto dto
    ) {
        log.info("Received request to validate certificate by code");
        CertificateValidationResponseDto response = validationService.validateByCode(dto.certificateCode());
        return ResponseEntity.ok(response);
    }

    /**
     * Valida certificado por upload de PDF
     * Endpoint público - não requer autenticação
     * Verifica assinatura digital e integridade dos dados
     */
    @Override
    @PostMapping(value = "/validate/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CertificateValidationResponseDto> validateByPdf(
            @RequestPart("file") MultipartFile file
    ) {
        log.info("Received request to validate certificate by PDF upload");
        CertificateValidationResponseDto response = validationService.validateByPdf(file);
        return ResponseEntity.ok(response);
    }
}