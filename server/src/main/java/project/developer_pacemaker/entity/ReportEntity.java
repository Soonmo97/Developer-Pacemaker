package project.developer_pacemaker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="report")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rSeq")
    private long rSeq;

    @ManyToOne
    @JoinColumn(name = "uSeq", referencedColumnName = "uSeq", nullable = false)
    private UserEntity user;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "totalDuration", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private Float totalDuration;

    @CreationTimestamp
    @Column(name = "registered", nullable = false)
    private String registered;

    @Column(name = "isDeleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;
}
