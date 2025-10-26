package ifpr.edu.br.mooc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificado")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "enrollment")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inscricao_id", nullable = false)
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscricao_id", insertable = false, updatable = false)
    private Enrollment enrollment;

    @Column(name = "nome_aluno", nullable = false)
    private String studentName;

    @Column(name = "cpf_aluno", nullable = false, length = 11)
    private String studentCpf;

    @Column(name = "nome_curso", nullable = false)
    private String courseName;

    @Column(name = "carga_horaria", nullable = false, columnDefinition = "TEXT")
    private String workload;

    @Column(name = "nome_campus", nullable = false)
    private String campusName;

    @Column(name = "data_conclusao", nullable = false)
    private LocalDateTime completionDate;

    @Column(name = "hash_documento", nullable = false, length = 500)
    private String documentHash;

    @Column(name = "assinatura_digital", nullable = false, length = 500)
    private String digitalSignature;

    @Column(name = "chave_publica", nullable = false, length = 500)
    private String publicKey;
}