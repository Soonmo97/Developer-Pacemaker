package project.developer_pacemaker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="gpt")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gSeq")
    private long gSeq;
    @ManyToOne
    @JoinColumn(name="uSeq", nullable = false)
    private UserEntity user;

    @Column(name="question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name="answer", nullable = false, columnDefinition = "TEXT")
    private String answer;
}
