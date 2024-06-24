package project.developer_pacemaker.dto.joinRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDTO {
    private long jSeq;
    private long sgSeq;
    private long uSeq;
    private String registered;
    private String nickname;
}
