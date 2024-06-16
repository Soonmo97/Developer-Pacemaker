package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "groupPlanner")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupPlannerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gpSeq")
    private long gpSeq;

    @ManyToOne
    @JoinColumn(name = "uSeq", referencedColumnName = "uSeq", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "sgSeq", referencedColumnName = "sgSeq", nullable = false)
    private StudyGroupEntity studyGroup;

    @CreationTimestamp
    @Column(name = "registered", nullable = false)
    private LocalDate registered;

    @Column(name = "isDeleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "groupPlanner")
    @JsonBackReference
    private List<GroupTodoEntity> groupTodoEntityList;

}
