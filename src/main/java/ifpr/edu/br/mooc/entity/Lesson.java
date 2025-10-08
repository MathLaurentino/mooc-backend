package ifpr.edu.br.mooc.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "aula")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "course")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "curso_id", nullable = false)
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", insertable = false, updatable = false)
    private Course course;

    @Column(name = "titulo", nullable = false, length = 100)
    private String title;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "miniatura")
    private String thumbnail;

    @Column(name = "url_video", nullable = false)
    private String videoUrl;

    @Column(name = "ordem_aula", nullable = false)
    private Integer lessonOrder;

    @Column(name = "criado_em", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "atualizado_em", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}