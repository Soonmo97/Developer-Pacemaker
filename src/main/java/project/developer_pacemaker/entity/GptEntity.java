package project.developer_pacemaker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="gpt")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gSeq")
    private long gSeq;
    @ManyToOne
    @JoinColumn(name="uSeq",  referencedColumnName = "uSeq", nullable = false)
    private UserEntity user;

    @Column(name="question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name="answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name="isDeleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

}
