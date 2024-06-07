package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uSeq", updatable = false)
    private long uSeq;

    @Column(name="pw", nullable = true, length = 100)
    private String pw;

    @Column(name="nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name="email", nullable = false, length = 50)
    private String email;

    @Column(name="img", nullable = true, length = 255)
    private String img;

    @Column(name="social", nullable = true, length = 20)
    private String social;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<GptEntity> gptEntities;
}

