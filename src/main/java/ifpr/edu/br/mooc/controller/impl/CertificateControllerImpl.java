package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.CertificateController;
import ifpr.edu.br.mooc.dto.certificate.CertificateResponseDto;
import ifpr.edu.br.mooc.dto.certificate.GenerateCertificateRequestDto;
import ifpr.edu.br.mooc.service.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@Slf4j
public class CertificateControllerImpl implements CertificateController {

    private final CertificateService certificateService;

    @Override
    @PostMapping("/generate")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CertificateResponseDto> generateCertificate(
            @RequestBody @Valid GenerateCertificateRequestDto dto
    ) {
        log.info("Received request to generate certificate for enrollment: {}", dto.enrollmentId());
        CertificateResponseDto response = certificateService.generateCertificate(dto.enrollmentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}