package project.developer_pacemaker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentBoardDTO {
    private long sgSeq;
    private String content;
    private String title;
}
