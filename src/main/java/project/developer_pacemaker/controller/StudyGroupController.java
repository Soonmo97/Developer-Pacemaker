package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.java.Log;
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

    @Operation(summary = "모집 중인 스터디그룹 전체 조회", description = "모집 중인 스터디그룹 전체 조회 API 입니다.")
    @GetMapping("/openAll")
    public ResponseEntity<?> getOpenAll() {
        try {
            List <StudyGroupEntity> studyGroups = studyGroupService.getOpenAll();
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
                .goal(studyGroupDTO.getGoal())
                .user(user)
                .build();

            StudyGroupEntity createStudyGroup = studyGroupService.create(studyGroup);
            StudyGroupDTO responseStudyGroupDTO = studyGroupDTO.builder()
                .sgSeq(createStudyGroup.getSgSeq())
                .name(createStudyGroup.getName())
                .goal(createStudyGroup.getGoal())
                .uSeq(createStudyGroup.getUser().getUSeq())
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

    @Operation(summary = "스터디그룹 수정(이름)", description = "스터디그룹 수정(이름) API 입니다. {sgSeq, name}")
    @PatchMapping("/name")
    public ResponseEntity<?> patchName(@AuthenticationPrincipal String uSeq,
        @RequestBody StudyGroupDTO studyGroupDTO) {
        try {
            StudyGroupEntity studyGroupEntity = studyGroupService.updateName(uSeq, studyGroupDTO);
            StudyGroupDTO resStudyGroupDTO = StudyGroupDTO.builder()
                .sgSeq(studyGroupEntity.getSgSeq())
                .name(studyGroupEntity.getName())
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

    @Operation(summary = "스터디그룹 수정(공동목표)", description = "스터디그룹 수정(공동목표) API 입니다. {sgSeq, goal}")
    @PatchMapping("/goal")
    public ResponseEntity<?> patchGoal(@AuthenticationPrincipal String uSeq,
                                   @RequestBody StudyGroupDTO studyGroupDTO) {
        try {
            StudyGroupEntity studyGroupEntity = studyGroupService.updateGoal(uSeq, studyGroupDTO);
            StudyGroupDTO resStudyGroupDTO = StudyGroupDTO.builder()
                .sgSeq(studyGroupEntity.getSgSeq())
                .goal(studyGroupEntity.getName())
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


    @Operation(summary = "스터디그룹 모집여부 조회", description = "스터디그룹 모집여부 조회 API 입니다.")
    @GetMapping("/status/{sgSeq}")
    public ResponseEntity<?> getStatus(@PathVariable String sgSeq) {
        try {
            boolean isDuplicate = studyGroupService.getStatus(Long.parseLong(sgSeq));
            return ResponseEntity.ok(isDuplicate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }


    @Operation(summary = "스터디그룹 모집여부 수정(활성, 비활성화)", description = "스터디그룹 모집여부 수정(활성, 비활성화) API 입니다. {sgSeq}")
    @PatchMapping("/status")
    public ResponseEntity<?> patchStatus(@AuthenticationPrincipal String uSeq,
                                       @RequestBody StudyGroupDTO studyGroupDTO) {
        try {
            StudyGroupEntity studyGroupEntity = studyGroupService.updateStatus(uSeq, studyGroupDTO);
            StudyGroupDTO resStudyGroupDTO = StudyGroupDTO.builder()
                .sgSeq(studyGroupEntity.getSgSeq())
                .status(studyGroupEntity.isStatus())
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



    @Operation(summary = "스터디그룹 삭제", description = "스터디그룹 삭제 API 입니다. {sgSeq}")
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

    @Operation(summary = "스터디그룹 그룹장 위임하기", description = "스터디그룹 그룹장 위임하기 API 입니다. {sgSeq, newUSeq}")
    @PatchMapping("/change-uSeq")
    public ResponseEntity<?> changeUSeq(@AuthenticationPrincipal String uSeq,
                                        @RequestBody StudyGroupDTO studyGroupDTO
                                        ) {
        try {
            StudyGroupEntity studyGroup = studyGroupService.changeUSeq(uSeq, studyGroupDTO.getSgSeq(), studyGroupDTO.getNewUSeq());
            return ResponseEntity.ok(studyGroup);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "내가 그룹장인 스터디그룹 존재여부 확인", description = "내가 그룹장인 스터디그룹 존재여부 확인 API 입니다..")
    @GetMapping("/check-uSeq")
    public ResponseEntity<?> checkUSeq(@AuthenticationPrincipal String uSeq) {
        try {
            boolean result = studyGroupService.checkUSeq(Long.parseLong(uSeq));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }

    }

    @Operation(summary = "내가 스터디그룹 그룹장인지 확인", description = "내가 스터디그룹 그룹장인지 확인 API 입니다. {sgSeq}")
    @GetMapping("/check-uSeq-me/{sgSeq}")
    public ResponseEntity<?> checkUSeqMe(@AuthenticationPrincipal String uSeq, @PathVariable String sgSeq) {
        try {
            boolean result = studyGroupService.checkUSeqMe(Long.parseLong(uSeq), Long.parseLong(sgSeq));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "내가 그룹장인 스터디그룹 리스트 조회", description = "내가 그룹장인 스터디그룹 리스트 조회 API 입니다.")
    @GetMapping("/leader-groups")
    public ResponseEntity<?> getUSeqGroups(@AuthenticationPrincipal String uSeq) {
        try {
            List<StudyGroupEntity> studyGroupEntities = studyGroupService.getUSeqAll(uSeq);
            return ResponseEntity.ok(studyGroupEntities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }
}
