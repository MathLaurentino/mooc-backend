package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.CertificateRequest;
import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CertificateRequestRepository extends JpaRepository<CertificateRequest, String>, JpaSpecificationExecutor<CertificateRequest> {

    boolean existsByEnrollmentId(Long enrollmentId);

    Optional<CertificateRequest> findByEnrollmentId(Long enrollmentId);

    long countByStatus(CertificateRequestStatus status);
}