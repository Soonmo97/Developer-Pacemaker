package project.developer_pacemaker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "todo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tSeq")
    private long tSeq;

    @ManyToOne
    @JoinColumn(name = "pSeq", referencedColumnName = "pSeq", nullable = false)
    private PlannerEntity planner;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Column(name = "duration", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private Float duration;

    @Column(name = "isCompleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isCompleted;

    @Column(name = "isDeleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

}
