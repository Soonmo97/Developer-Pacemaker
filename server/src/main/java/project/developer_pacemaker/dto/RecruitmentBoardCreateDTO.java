package project.developer_pacemaker.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.developer_pacemaker.entity.StudyGroupEntity;

@Getter
@Builder
public class RecruitmentBoardCreateDTO {
    private String nickname;
    private StudyGroupEntity studyGroup;
    private String content;
    private String name;
    private String registered;
}