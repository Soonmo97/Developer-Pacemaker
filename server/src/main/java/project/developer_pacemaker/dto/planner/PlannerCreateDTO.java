package project.developer_pacemaker.dto.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlannerCreateDTO {
    private List<TodoDTO> todoDTOList;
}
