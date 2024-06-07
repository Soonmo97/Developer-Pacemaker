package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.developer_pacemaker.dto.ResErrorDTO;
import project.developer_pacemaker.dto.StudyGroupDTO;
import project.developer_pacemaker.dto.UserDTO;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.service.StudyGroupService;

@RestController
@RequestMapping("/api/studyGroup")
public class StudyGroupController {

    final private StudyGroupService studyGroupService;

    @Autowired
    public StudyGroupController(final StudyGroupService studyGroupService) {
        this.studyGroupService = studyGroupService;
    }

    @Operation(summary = "스터디그룹 생성", description = "스터디그룹 생성 API 입니다.")
    @PostMapping()
    public ResponseEntity<?> create(@AuthenticationPrincipal String uSeq,
                                    @RequestBody StudyGroupDTO studyGroupDTO) {
        try {
            UserEntity user = studyGroupService.findByUSeq(uSeq);
            StudyGroupEntity studyGroup = StudyGroupEntity.builder()
                .name(studyGroupDTO.getName())
                .user(user)
                .max(studyGroupDTO.getMax())
                .build();

            StudyGroupEntity createStudyGroup = studyGroupService.create(studyGroup);
            StudyGroupDTO responseStudyGroupDTO = studyGroupDTO.builder()
                .name(createStudyGroup.getName())
                .uSeq(createStudyGroup.getUser().getUSeq())
                .max(createStudyGroup.getMax())
                .registered(createStudyGroup.getRegistered())
                .build();

            return ResponseEntity.ok(responseStudyGroupDTO);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

}
