package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, String> {

    @Query("SELECT c FROM Certificate c WHERE c.enrollmentId = :enrollmentId ORDER BY c.createdAt DESC")
    Optional<Certificate> findLatestByEnrollmentId(@Param("enrollmentId") Long enrollmentId);

    boolean existsByEnrollmentId(Long enrollmentId);

    Optional<Certificate> findByDocumentHash(String documentHash);
}