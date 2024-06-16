package project.developer_pacemaker.dto.groupPlanner;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupPlannerCreateDTO {
    private long sgSeq;
    private List<GroupTodoCreateDTO> groupTodoCreateDTOList;
}
