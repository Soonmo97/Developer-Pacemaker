package project.developer_pacemaker.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptControlDTO {
    private long gSeq;
    private String question;
    private String answer;
}
