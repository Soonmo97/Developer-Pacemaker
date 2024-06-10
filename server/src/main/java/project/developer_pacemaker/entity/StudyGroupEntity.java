package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@SQLDelete(sql = "UPDATE study_Group SET is_deleted = 1 WHERE sg_seq = ?") // jpa delete 를 실행시킬 경우 해당 sql 문 실행
@SQLRestriction("is_deleted = 0") // 해당 엔티티의 기본 쿼리에 디폴트로 where 조건을 적용하는 어노테이션
@Table(name = "studyGroup")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sgSeq", updatable = false)
    private long sgSeq;

    @Column(name="name", nullable = false, length = 30)
    private String name;

    @CreationTimestamp
    @Column(name="registered", nullable = false)
    private String registered;

    @Column(name="max", nullable = false)
    @ColumnDefault("15")
    private final int max = 15;

    @Column(name="current", nullable = true)
    private int current;

    @Column(name="status", nullable = false)
    @ColumnDefault("false")
    private boolean status;

    @Column(name="isDeleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "uSeq", nullable = false)
    @JsonBackReference
    private UserEntity user;

}
