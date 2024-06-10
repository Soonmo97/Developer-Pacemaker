package project.developer_pacemaker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyGroupDTO {
    private long sgSeq;
    private String name;
    private int max;
    private long uSeq;
    private String registered;
}
