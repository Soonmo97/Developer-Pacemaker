package project.developer_pacemaker.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportCreateDTO {
    private String title;
    private String content;
}
