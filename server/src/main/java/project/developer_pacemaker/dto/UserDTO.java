package project.developer_pacemaker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {
    private String nickname;
    private String email;
    private String pw;
    private String img;
    private String social;
    private String token;
    private long uSeq;
}
