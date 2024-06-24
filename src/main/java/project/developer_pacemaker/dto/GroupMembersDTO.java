package project.developer_pacemaker.dto;

import lombok.Builder;
import lombok.Getter;
import project.developer_pacemaker.entity.UserEntity;

import java.util.List;

@Getter
@Builder
public class GroupMembersDTO {
    private long uSeq;
    private long sgSeq;
    private String nickname;
    private String img;
    private int score;
}
