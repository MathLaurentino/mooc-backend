package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.certificate.CertificateResponseDto;
import ifpr.edu.br.mooc.entity.Certificate;
import ifpr.edu.br.mooc.entity.CertificateRequest;
import ifpr.edu.br.mooc.entity.Enrollment;
import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import ifpr.edu.br.mooc.exceptions.base.BadRequestException;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.exceptions.base.UnauthorizedException;
import ifpr.edu.br.mooc.mapper.CertificateMapper;
import ifpr.edu.br.mooc.repository.CertificateRepository;
import ifpr.edu.br.mooc.repository.CertificateRequestRepository;
import ifpr.edu.br.mooc.repository.EnrollmentRepository;
import ifpr.edu.br.mooc.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CertificateRequestRepository certificateRequestRepository;
    private final CryptographyService cryptographyService;
    private final CurrentUserService currentUserService;
    private final CertificateMapper mapper;

    @Value("${server.base-url:http://localhost:8080}")
    private String baseUrl;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Transactional
    public CertificateResponseDto generateCertificate(Long enrollmentId) {
        log.info("Generating certificate for enrollment: {}", enrollmentId);

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

        // 5. Capturar dados do momento da emissão
        String studentName = enrollment.getUser().getFullName();
        String studentCpf = enrollment.getUser().getCpf();
        String courseName = enrollment.getCourse().getName();
        String workload = enrollment.getCourse().getWorkload().toString();
        String campusName = enrollment.getCourse().getCampus().getName();
        String completionDate = enrollment.getCompletedAt().format(DATE_FORMATTER);

        // 6. Gerar hash criptográfico
        String certificateData = cryptographyService.buildCertificateData(
                studentName,
                studentCpf,
                courseName,
                workload,
                campusName,
                completionDate
        );
        String documentHash = cryptographyService.generateHash(certificateData);

        // 7. Assinar digitalmente
        String digitalSignature = cryptographyService.signHash(documentHash);

        // 8. Obter chave pública
        String publicKey = cryptographyService.getPublicKeyAsString();

        // 9. Criar registro do certificado
        Certificate certificate = Certificate.builder()
                .enrollmentId(enrollmentId)
                .enrollment(enrollment)
                .studentName(studentName)
                .studentCpf(studentCpf)
                .courseName(courseName)
                .workload(workload)
                .campusName(campusName)
                .completionDate(enrollment.getCompletedAt())
                .documentHash(documentHash)
                .digitalSignature(digitalSignature)
                .publicKey(publicKey)
                .build();

        Certificate savedCertificate = certificateRepository.save(certificate);

        log.info("Certificate generated successfully with id: {}", savedCertificate.getId());

        // 10. Retornar DTO com URL do certificado
        CertificateResponseDto response = mapper.toDto(savedCertificate);
        String certificateUrl = generateCertificateUrl(savedCertificate.getId());

        return new CertificateResponseDto(
                response.id(),
                response.enrollmentId(),
                response.studentName(),
                response.studentCpf(),
                response.courseName(),
                response.workload(),
                response.campusName(),
                response.completionDate(),
                response.documentHash(),
                response.digitalSignature(),
                response.publicKey(),
                certificateUrl
        );
    }

    private String generateCertificateUrl(Long certificateId) {
        return String.format("%s/mooc/certificates/%d", baseUrl, certificateId);
    }
}