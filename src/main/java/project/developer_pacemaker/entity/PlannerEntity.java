package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "planner")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlannerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pSeq")
    private long pSeq;

    @ManyToOne
    @JoinColumn(name = "uSeq", referencedColumnName = "uSeq", nullable = false)
    private UserEntity user;

    @Column(name = "registered", nullable = false)
    private LocalDate registered;

    @Column(name="memo", nullable = false, columnDefinition = "TEXT")
    private String memo;

    @Column(name = "isDeleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "planner")
    @JsonBackReference
    private List<TodoEntity> todoEntities;
}
