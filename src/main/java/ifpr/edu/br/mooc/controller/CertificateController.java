package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.certificate.CertificateResponseDto;
import ifpr.edu.br.mooc.dto.certificate.GenerateCertificateRequestDto;
import org.springframework.http.ResponseEntity;

public interface CertificateController {

    ResponseEntity<CertificateResponseDto> generateCertificate(GenerateCertificateRequestDto dto);
}