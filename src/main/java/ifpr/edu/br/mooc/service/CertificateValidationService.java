package ifpr.edu.br.mooc.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfReader;
import ifpr.edu.br.mooc.dto.certificate.CertificateValidationResponseDto;
import ifpr.edu.br.mooc.entity.Certificate;
import ifpr.edu.br.mooc.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateValidationService {

    private final CertificateRepository certificateRepository;
    private final CryptographyService cryptographyService;

    /**
     * Valida certificado pelo código (UUID)
     */
    @Transactional(readOnly = true)
    public CertificateValidationResponseDto validateByCode(String certificateCode) {
        log.info("Validating certificate by code: {}", certificateCode);

        // Buscar certificado no banco de dados
        Optional<Certificate> certificateOpt = certificateRepository.findById(certificateCode);

        if (certificateOpt.isEmpty()) {
            log.warn("Certificate not found for code: {}", certificateCode);
            return CertificateValidationResponseDto.invalid(
                    "Certificado não encontrado. Verifique se o código foi digitado corretamente."
            );
        }

        Certificate certificate = certificateOpt.get();

        // Certificado encontrado - retornar dados
        log.info("Certificate found and validated: {}", certificateCode);
        return CertificateValidationResponseDto.valid(
                certificate.getStudentName(),
                certificate.getStudentCpf(),
                certificate.getCourseName(),
                certificate.getWorkload(),
                certificate.getCampusName(),
                certificate.getCompletionDate()
        );
    }

    /**
     * Valida certificado por upload do PDF
     * 1. Extrai metadados do PDF
     * 2. Recalcula hash dos dados extraídos
     * 3. Usa chave pública para descriptografar assinatura
     * 4. Compara hash recalculado com hash da assinatura
     */
    @Transactional(readOnly = true)
    public CertificateValidationResponseDto validateByPdf(MultipartFile file) {
        log.info("Validating certificate by PDF upload");

        try {
            // Validar se é arquivo PDF
            if (file.isEmpty()) {
                return CertificateValidationResponseDto.invalid("Arquivo vazio");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                return CertificateValidationResponseDto.invalid(
                        "O arquivo enviado não é um PDF válido"
                );
            }

            // Ler PDF e extrair metadados
            byte[] pdfBytes = file.getBytes();
            PdfReader reader = new PdfReader(new ByteArrayInputStream(pdfBytes));
            PdfDocument pdfDocument = new PdfDocument(reader);
            PdfDocumentInfo info = pdfDocument.getDocumentInfo();

            // Extrair dados dos metadados customizados
            String studentName = info.getMoreInfo("StudentName");
            String studentCpf = info.getMoreInfo("StudentCpf");
            String courseName = info.getMoreInfo("CourseName");
            String workload = info.getMoreInfo("Workload");
            String campusName = info.getMoreInfo("CampusName");
            String completionDate = info.getMoreInfo("CompletionDate");
            String algorithm = info.getMoreInfo("Algorithm");
            String extractedHash = info.getMoreInfo("Hash");
            String signature = info.getMoreInfo("Signature");
            String publicKey = info.getMoreInfo("PublicKey");

            pdfDocument.close();

            // Validar se todos os metadados necessários existem
            if (studentName == null || studentCpf == null || courseName == null || 
                workload == null || campusName == null || completionDate == null ||
                extractedHash == null || signature == null || publicKey == null) {
                
                log.warn("PDF metadata incomplete or missing");
                return CertificateValidationResponseDto.invalid(
                        "PDF não contém os metadados de verificação necessários. " +
                        "Certifique-se de que o arquivo é um certificado válido emitido pelo sistema."
                );
            }

            // Validar algoritmo
            if (!"SHA256withRSA".equals(algorithm)) {
                log.warn("Invalid algorithm in PDF: {}", algorithm);
                return CertificateValidationResponseDto.invalid(
                        "Algoritmo de criptografia do certificado não é válido"
                );
            }

            // PASSO 1: Recalcular hash dos dados extraídos
            String certificateData = cryptographyService.buildCertificateData(
                    studentName,
                    studentCpf,
                    courseName,
                    workload,
                    campusName,
                    completionDate
            );
            String recalculatedHash = cryptographyService.generateHash(certificateData);

            log.info("Hash extracted from PDF: {}", extractedHash.substring(0, 20) + "...");
            log.info("Hash recalculated: {}", recalculatedHash.substring(0, 20) + "...");

            // Verificar se hash recalculado bate com hash extraído
            if (!recalculatedHash.equals(extractedHash)) {
                log.warn("Recalculated hash does not match extracted hash - data may have been tampered");
                return CertificateValidationResponseDto.invalid(
                        "Os dados do certificado foram alterados. " +
                        "O hash recalculado não corresponde ao hash original."
                );
            }

            // PASSO 2: Verificar assinatura digital usando a chave pública
            boolean signatureValid = cryptographyService.verifySignature(
                    extractedHash,
                    signature,
                    publicKey
            );

            if (!signatureValid) {
                log.warn("Digital signature verification failed");
                return CertificateValidationResponseDto.invalid(
                        "Assinatura digital inválida. " +
                        "O certificado pode ter sido falsificado ou corrompido."
                );
            }

            // Certificado válido!
            log.info("Certificate validated successfully via PDF");
            
            // Converter completionDate de String para LocalDateTime
            // Formato esperado: "dd/MM/yyyy"
            java.time.LocalDateTime completionDateTime;
            try {
                java.time.format.DateTimeFormatter formatter = 
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.time.LocalDate date = java.time.LocalDate.parse(completionDate, formatter);
                completionDateTime = date.atStartOfDay();
            } catch (Exception e) {
                log.error("Error parsing completion date: {}", completionDate, e);
                completionDateTime = null;
            }

            return CertificateValidationResponseDto.valid(
                    studentName,
                    studentCpf,
                    courseName,
                    workload,
                    campusName,
                    completionDateTime
            );

        } catch (Exception e) {
            log.error("Error validating certificate by PDF", e);
            return CertificateValidationResponseDto.invalid(
                    "Erro ao processar o arquivo PDF. Certifique-se de que é um certificado válido."
            );
        }
    }
}