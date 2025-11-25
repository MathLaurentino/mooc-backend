package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.entity.Certificate;
import ifpr.edu.br.mooc.entity.CertificateRequest;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import ifpr.edu.br.mooc.exceptions.base.BadRequestException;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.exceptions.base.UnauthorizedException;
import ifpr.edu.br.mooc.repository.CertificateRepository;
import ifpr.edu.br.mooc.repository.CertificateRequestRepository;
import ifpr.edu.br.mooc.repository.EnrollmentRepository;
import ifpr.edu.br.mooc.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CertificateRequestRepository certificateRequestRepository;
    private final CryptographyService cryptographyService;
    private final PdfGeneratorService pdfGeneratorService;
    private final CurrentUserService currentUserService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Endpoint único que:
     * 1. Verifica se existe certificado para o enrollment
     * 2. Se existir, compara hash dos dados atuais com o hash armazenado
     * 3. Se hash for igual, retorna PDF do certificado existente
     * 4. Se hash for diferente, cria novo registro e retorna PDF
     * 5. Se não existir, cria novo registro e retorna PDF
     */
    @Transactional
    public byte[] generateOrGetCertificatePdf(Long enrollmentId) {
        log.info("Generating or retrieving certificate for enrollment: {}", enrollmentId);

        // 1. Buscar a inscrição
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NotFoundException("Inscrição não encontrada"));

        // 2. Verificar se o usuário atual é o dono da inscrição
        Long currentUserId = currentUserService.getCurrentUserId();
        if (!enrollment.getUserId().equals(currentUserId)) {
            throw new UnauthorizedException("Você não tem permissão para gerar este certificado");
        }

        // 3. Verificar se o curso foi concluído
        if (!enrollment.getCompleted()) {
            throw new BadRequestException("O curso ainda não foi concluído");
        }

        // 4. Verificar se existe uma solicitação de certificado aprovada
        CertificateRequest certificateRequest = certificateRequestRepository
                .findByEnrollmentId(enrollmentId)
                .orElseThrow(() -> new NotFoundException("Solicitação de certificado não encontrada"));

        if (certificateRequest.getStatus() != CertificateRequestStatus.APPROVED) {
            throw new BadRequestException(
                    String.format("Não é possível gerar o certificado. Status da solicitação: %s",
                            certificateRequest.getStatus().getDescription())
            );
        }

        // 5. Capturar dados atuais do enrollment
        String studentName = enrollment.getUser().getFullName();
        String studentCpf = enrollment.getUser().getCpf();
        String courseName = enrollment.getCourse().getName();
        String workload = enrollment.getCourse().getWorkload().toString();
        String campusName = enrollment.getCourse().getCampus().getName();
        String completionDate = enrollment.getCompletedAt().format(DATE_FORMATTER);

        // 6. Gerar hash dos dados atuais
        String certificateData = cryptographyService.buildCertificateData(
                studentName,
                studentCpf,
                courseName,
                workload,
                campusName,
                completionDate
        );
        String currentHash = cryptographyService.generateHash(certificateData);

        // 7. Verificar se já existe certificado para este enrollment
        Optional<Certificate> existingCertificate = certificateRepository.findLatestByEnrollmentId(enrollmentId);

        Certificate certificate;

        if (existingCertificate.isPresent()) {
            Certificate existing = existingCertificate.get();

            // 8. Comparar hash atual com hash armazenado
            if (existing.getDocumentHash().equals(currentHash)) {
                // Hash é o mesmo - dados não mudaram
                log.info("Certificate already exists with same data. Using existing certificate: {}", existing.getId());
                certificate = existing;
            } else {
                // Hash é diferente - dados mudaram, criar novo registro
                log.info("Data changed since last certificate. Creating new certificate for enrollment: {}", enrollmentId);
                certificate = createNewCertificate(enrollment, studentName, studentCpf, courseName,
                        workload, campusName, completionDate, currentHash);
            }
        } else {
            // 9. Não existe certificado - criar novo
            log.info("No certificate found. Creating new certificate for enrollment: {}", enrollmentId);
            certificate = createNewCertificate(enrollment, studentName, studentCpf, courseName,
                    workload, campusName, completionDate, currentHash);
        }

        // 10. Gerar e retornar PDF
        return pdfGeneratorService.generateCertificatePdf(certificate);
    }

    /**
     * Cria um novo registro de certificado
     */
    private Certificate createNewCertificate(
            Enrollment enrollment,
            String studentName,
            String studentCpf,
            String courseName,
            String workload,
            String campusName,
            String completionDate,
            String documentHash
    ) {
        // Assinar digitalmente
        String digitalSignature = cryptographyService.signHash(documentHash);

        // Criar registro do certificado
        Certificate certificate = Certificate.builder()
                .enrollmentId(enrollment.getId())
                .enrollment(enrollment)
                .studentName(studentName)
                .studentCpf(studentCpf)
                .courseName(courseName)
                .workload(workload)
                .campusName(campusName)
                .completionDate(enrollment.getCompletedAt())
                .documentHash(documentHash)
                .digitalSignature(digitalSignature)
                .build();

        Certificate savedCertificate = certificateRepository.save(certificate);
        log.info("New certificate created with id: {}", savedCertificate.getId());

        return savedCertificate;
    }
}