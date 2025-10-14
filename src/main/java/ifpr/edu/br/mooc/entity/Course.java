package ifpr.edu.br.mooc.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curso")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"campus", "knowledgeArea", "lessons"})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "area_conhecimento_id", nullable = false)
    private Long knowledgeAreaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_conhecimento_id", insertable = false, updatable = false)
    private KnowledgeArea knowledgeArea;

    @Column(name = "campus_id", nullable = false)
    private Long campusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", insertable = false, updatable = false)
    private Campus campus;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Lesson> lessons = new ArrayList<>();

    @Column(name = "nome_professor", nullable = false)
    private String professorName;

    @Column(name = "miniatura")
    private String thumbnail;

    @Column(name = "carga_horaria", nullable = false)
    private Integer workload;

    @Column(name = "visivel", nullable = false)
    @Builder.Default
    private Boolean visible = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "atualizado_em", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}