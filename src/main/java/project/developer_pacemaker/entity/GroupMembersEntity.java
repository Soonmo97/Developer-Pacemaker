package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE group_members SET is_deleted = 1 WHERE m_seq = ?") // jpa delete 를 실행시킬 경우 해당 sql 문 실행
@SQLRestriction("is_deleted = 0") // 해당 엔티티의 기본 쿼리에 디폴트로 where 조건을 적용하는 어노테이션
@Table(name = "group_members")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMembersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mSeq", updatable = false)
    private long mSeq;

    @CreationTimestamp
    @Column(name="registered", nullable = false)
    private String registered;

    @Column(name = "score", nullable = false)
    @ColumnDefault("0")
    private int score;

    @Column(name="isDeleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "sgSeq", nullable = false)
    @JsonBackReference
    private StudyGroupEntity studyGroup;

    @ManyToOne
    @JoinColumn(name = "uSeq", nullable = false)
    @JsonBackReference
    private UserEntity user;

}
