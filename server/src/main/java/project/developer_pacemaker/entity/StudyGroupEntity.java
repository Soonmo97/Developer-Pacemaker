package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Entity
@SQLDelete(sql = "UPDATE study_group SET is_deleted = 1 WHERE sg_seq = ?") // jpa delete 를 실행시킬 경우 해당 sql 문 실행
@SQLRestriction("is_deleted = 0") // 해당 엔티티의 기본 쿼리에 디폴트로 where 조건을 적용하는 어노테이션
@Table(name = "study_group")
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

    @Column(name="name", nullable = false, length = 20)
    private String name;

    @CreationTimestamp
    @Column(name="registered", nullable = false)
    private String registered;

    @Column(name="current", nullable = true)
    private int current;

    @Column(name="status", nullable = false)
    @ColumnDefault("false")
    private boolean status;

    @Column(name="isDeleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @Column(name="goal", nullable = false, length = 20)
    private String goal;

    @ManyToOne
    @JoinColumn(name = "uSeq", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @OneToMany(mappedBy = "studyGroup")
    @JsonManagedReference
    private List<RecruitmentBoardEntity> recruitmentBoards;
  
    @OneToMany(mappedBy = "studyGroup")
    @JsonBackReference
    private List<JoinRequestEntity> joinRequestEntityList;

    @OneToMany(mappedBy = "studyGroup")
    @JsonBackReference
    private List<GroupMembersEntity> groupMembers;

    @OneToMany(mappedBy = "studyGroup")
    @JsonBackReference
    private List<GroupPlannerEntity> groupPlannerEntityList;
}
