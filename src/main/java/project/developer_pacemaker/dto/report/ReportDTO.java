package project.developer_pacemaker.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private long rSeq;
    private String title;
    private String content;
    private String registered;

}
