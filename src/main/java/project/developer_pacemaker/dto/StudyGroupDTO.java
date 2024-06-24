package project.developer_pacemaker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyGroupDTO {
    private long sgSeq;
    private String name;
    private long uSeq;
    private String registered;
    private String goal;
    private boolean status;
    private long newUSeq;
}
