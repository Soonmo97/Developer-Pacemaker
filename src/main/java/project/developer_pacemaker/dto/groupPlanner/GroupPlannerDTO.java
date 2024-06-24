package project.developer_pacemaker.dto.groupPlanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupPlannerDTO {
    private long gpSeq;
    private LocalDate registered;
    private List<GroupTodoDTO> groupTodoDTOList;
}
