package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    @Column(name = "registered", nullable = false)
    private String registered;

    @Column(name = "isDeleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "planner")
    @JsonBackReference
    private List<TodoEntity> todoEntities;
}
