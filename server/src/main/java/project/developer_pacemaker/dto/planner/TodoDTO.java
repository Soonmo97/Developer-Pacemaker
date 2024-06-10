package project.developer_pacemaker.dto.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private String content;
    private Float duration;
    private Boolean isCompleted;
}
