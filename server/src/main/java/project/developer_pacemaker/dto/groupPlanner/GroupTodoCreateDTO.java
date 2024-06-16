package project.developer_pacemaker.dto.groupPlanner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupTodoCreateDTO {
    private String content;
    private Boolean isCompleted;
}
