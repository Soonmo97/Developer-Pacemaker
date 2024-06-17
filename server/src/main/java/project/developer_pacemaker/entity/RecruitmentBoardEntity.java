package project.developer_pacemaker.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;



@Entity
@SQLDelete(sql = "UPDATE recruitmentBoard SET is_deleted = 1 WHERE rbSeq = ?") // jpa delete 를 실행시킬 경우 해당 sql 문 실행
@SQLRestriction("is_deleted = 0") // 해당 엔티티의 기본 쿼리에 디폴트로 where 조건을 적용하는 어노테이션
@Table(name = "recruitmentBoard")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rbSeq")
public class RecruitmentBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rbSeq", updatable = false)
    private long rbSeq;

    @ManyToOne
    @JoinColumn(name = "sgSeq", updatable = false)
    @JsonBackReference // 순환 참조 방지
//    @JsonIgnore // jsonIgnore 사용하면 studyGroup 필드 전체를 포함하는 것을 막아줌
    private StudyGroupEntity studyGroup;
//    @JsonProperty("sgSeq")
//    public Long getStudyGroupId() {
//        return studyGroup != null ? studyGroup.getSgSeq() : null;
//    }
    // sg_Seq 숫자 하나만 포함시켜서 응답하고 싶을 때 위 주석 해제

    @CreationTimestamp
    @Column(name = "registered", nullable = false)
    private String registered;

    @Column(name = "content", nullable = false, length = 30)
    private String content;

    @Column(name = "isDeleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @Column(name = "name", nullable = false, length = 30)
    private String name;  // 사용자가 직접 입력하는 제목
}
