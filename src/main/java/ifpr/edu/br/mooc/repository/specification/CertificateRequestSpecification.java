package ifpr.edu.br.mooc.repository.specification;

import ifpr.edu.br.mooc.entity.CertificateRequest;
import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CertificateRequestSpecification implements Specification<CertificateRequest> {

    private final CertificateRequestStatus status;

    public CertificateRequestSpecification(CertificateRequestStatus status) {
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<CertificateRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}