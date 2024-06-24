package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "joinRequest")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jSeq")
    private long jSeq;

    @Column(name = "uSeq", nullable = false)
    private long uSeq;

    @CreationTimestamp
    @Column(name = "registered", nullable = false)
    private String registered;

    @ManyToOne
    @JoinColumn(name = "sgSeq", referencedColumnName = "sgSeq", nullable = false)
    @JsonBackReference
    private StudyGroupEntity studyGroup;
}
