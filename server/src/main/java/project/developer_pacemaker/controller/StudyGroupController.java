package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.ResErrorDTO;
import project.developer_pacemaker.dto.StudyGroupDTO;
import project.developer_pacemaker.dto.UserDTO;
import project.developer_pacemaker.entity.GroupMembersEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.service.StudyGroupService;
import project.developer_pacemaker.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/study-group")
public class StudyGroupController {

    final private StudyGroupService studyGroupService;

    final private UserService userService;

    @Autowired
    public StudyGroupController(final StudyGroupService studyGroupService, final UserService userService) {
        this.studyGroupService = studyGroupService;
        this.userService = userService;
    }
    @Operation(summary = "스터디그룹 전체 조회", description = "스터디그룹 전체 조회 API 입니다.")
    @GetMapping()
    public ResponseEntity<?> getAll() {
        try {
            List <StudyGroupEntity> studyGroups = studyGroupService.getAll();
            return ResponseEntity.ok(studyGroups);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "내 스터디그룹 전체 조회", description = "내 스터디그룹 전체 조회 API 입니다.")
    @GetMapping("/me")
    public ResponseEntity<?> getMyAll(@AuthenticationPrincipal String uSeq) {
        try{
            List<StudyGroupEntity> myStudyGroups = studyGroupService.myGetAll(uSeq);
            return ResponseEntity.ok(myStudyGroups);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
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

    @Operation(summary = "스터디그룹 이름 중복체크", description = "스터디그룹 이름 중복체크 API 입니다.")
    @GetMapping("/check-name")
    public ResponseEntity<?> checkName(@RequestParam String name) {
        try {
            boolean isDuplicate = studyGroupService.isDuplicateName(name);
            return ResponseEntity.ok(isDuplicate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "스터디그룹 수정(이름)", description = "스터디그룹 수정(이름) API 입니다.")
    @PatchMapping()
    public ResponseEntity<?> patch(@AuthenticationPrincipal String uSeq,
        @RequestBody StudyGroupDTO studyGroupDTO) {
        try {
            StudyGroupEntity studyGroupEntity = studyGroupService.updateName(uSeq, studyGroupDTO);
            StudyGroupDTO resStudyGroupDTO = StudyGroupDTO.builder()
                .name(studyGroupEntity.getName())
                .max(studyGroupEntity.getMax())
                .uSeq(studyGroupEntity.getUser().getUSeq())
                .registered(studyGroupEntity.getRegistered())
                .build();

            return ResponseEntity.ok(resStudyGroupDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "스터디그룹 삭제", description = "스터디그룹 삭제 API 입니다.")
    @DeleteMapping()
    public ResponseEntity<?> delete(@AuthenticationPrincipal String uSeq,
        @RequestBody StudyGroupDTO studyGroupDTO) {
        try {
            StudyGroupEntity studyGroup = studyGroupService.delete(uSeq, studyGroupDTO.getSgSeq());

            StudyGroupDTO resStudyGroupDTO = StudyGroupDTO.builder()
                .sgSeq(studyGroup.getSgSeq())
                .name(studyGroup.getName())
                .uSeq(studyGroup.getUser().getUSeq())
                .build();
            return ResponseEntity.ok(resStudyGroupDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

}
