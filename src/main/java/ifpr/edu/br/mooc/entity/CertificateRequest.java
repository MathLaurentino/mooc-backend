package ifpr.edu.br.mooc.entity;

import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import ifpr.edu.br.mooc.entity.enums.converter.CertificateRequestStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitacao_certificado")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "enrollment")
public class CertificateRequest {

    @Id
    private String id;

    @Column(name = "inscricao_id", nullable = false)
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscricao_id", insertable = false, updatable = false)
    private Enrollment enrollment;

    @Column(name = "status", nullable = false, length = 20)
    @Convert(converter = CertificateRequestStatusConverter.class)
    @Builder.Default
    private CertificateRequestStatus status = CertificateRequestStatus.ANALYSIS;

    @Column(name = "criado_em", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "atualizado_em")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "motivo_reprovacao", columnDefinition = "TEXT")
    private String rejectionReason;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}