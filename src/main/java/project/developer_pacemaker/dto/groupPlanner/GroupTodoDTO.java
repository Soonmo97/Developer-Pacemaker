package project.developer_pacemaker.dto.groupPlanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupTodoDTO {
    private long gtSeq;
    private String content;
    private Boolean isCompleted;
}
