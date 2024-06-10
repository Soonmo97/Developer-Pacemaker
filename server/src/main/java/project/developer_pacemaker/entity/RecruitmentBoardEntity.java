package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE studyGroup SET is_deleted = 1 WHERE u_seq = ?") // jpa delete 를 실행시킬 경우 해당 sql 문 실행
@SQLRestriction("is_deleted = 0") // 해당 엔티티의 기본 쿼리에 디폴트로 where 조건을 적용하는 어노테이션
@Table(name = "recruitmentBoard")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rb_seq", updatable = false)
    private long rb_seq;

    @ManyToOne
    @Column(name = "sgSeq", updatable = false)
    @JsonManagedReference
    private StudyGroupEntity studyGroup;

    @CreationTimestamp
    @Column(name = "createdDate", nullable = false, updatable = false)
    private Data createdDate;

    @Column(name = "content", nullable = false, length = 30)
    private String content;

    @Column(name = "isDeleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;
}
