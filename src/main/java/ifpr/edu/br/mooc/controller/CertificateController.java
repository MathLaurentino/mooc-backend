package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.certificate.CertificateValidationResponseDto;
import ifpr.edu.br.mooc.dto.certificate.GenerateCertificateRequestDto;
import ifpr.edu.br.mooc.dto.certificate.ValidateCertificateByCodeRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CertificateController {

    ResponseEntity<byte[]> generateOrDownloadCertificate(GenerateCertificateRequestDto dto);

    ResponseEntity<CertificateValidationResponseDto> validateByCode(ValidateCertificateByCodeRequestDto dto);

    ResponseEntity<CertificateValidationResponseDto> validateByCode(String dto);

    ResponseEntity<CertificateValidationResponseDto> validateByPdf(MultipartFile file);

}