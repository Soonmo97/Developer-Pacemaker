package project.developer_pacemaker.dto.groupPlanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupPlannerControllDTO {
    private long gpSeq;
    private long gtSeq;
    private String content;
    private Boolean isCompleted;
}
