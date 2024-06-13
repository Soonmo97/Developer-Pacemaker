package project.developer_pacemaker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "groupTodo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupTodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gtSeq")
    private long gtSeq;

    @ManyToOne
    @JoinColumn(name = "gpSeq", referencedColumnName = "gpSeq", nullable = false)
    private GroupPlannerEntity groupPlanner;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Column(name = "isCompleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isCompleted;


}
