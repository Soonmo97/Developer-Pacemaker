package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@SQLDelete(sql = "UPDATE user SET is_deleted = 1 WHERE u_seq = ?") // jpa delete 를 실행시킬 경우 해당 sql 문 실행
@SQLRestriction("is_deleted = 0") // 해당 엔티티의 기본 쿼리에 디폴트로 where 조건을 적용하는 어노테이션
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

    @Column(name="isDeleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<GptEntity> gptEntities;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<ReportEntity> reportEntities;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<StudyGroupEntity> studyGroups;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<GroupMembersEntity> groupMembers;
  
    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<PlannerEntity> plannerEntities;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<GroupPlannerEntity> groupPlannerEntityList;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<StudyGroupGrassEntity> studyGroupGrassEntities;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<UserGrassEntity> userGrassEntities;
}

