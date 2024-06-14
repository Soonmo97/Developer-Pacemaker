package project.developer_pacemaker.dto.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlannerDTO {
    private long pSeq;
    private LocalDate registered;
    private List<TodoDTO> todoDTOList;
}
