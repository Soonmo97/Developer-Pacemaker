package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.GroupMembersDTO;
import project.developer_pacemaker.dto.ResErrorDTO;
import project.developer_pacemaker.dto.UserDTO;
import project.developer_pacemaker.entity.GroupMembersEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.service.GroupMembersService;

import java.util.List;

@RestController
@RequestMapping("/api/group-members")
public class GroupMembersController {

    final private GroupMembersService groupMembersService;

    @Autowired
    public GroupMembersController(final GroupMembersService groupMembersService) {
        this.groupMembersService = groupMembersService;
    }

    @Operation(summary = "스터디그룹 멤버 전체 조회", description = "스터디그룹 멤버 전체 조회 API 입니다.")
    @GetMapping("/{sgSeq}")
    public ResponseEntity<?> getGroupMembers(@PathVariable String sgSeq) {
        try {
            List<GroupMembersDTO> groupMembers = groupMembersService.getGroupMembersByStudyGroup(sgSeq);
            return ResponseEntity.ok(groupMembers);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "스터디그룹 멤버 강퇴(그룹장)", description = "스터디그룹 멤버 강퇴 API 입니다.")
    @DeleteMapping("/kick")
    public ResponseEntity<?> kickMember(
        @AuthenticationPrincipal String uSeq,
        @RequestBody GroupMembersDTO groupMembersDTO
    ) {
        try{
            UserEntity deleteUser = groupMembersService.kickMember(uSeq, groupMembersDTO.getSgSeq(), groupMembersDTO.getUSeq());
            UserDTO resDeleteUser = UserDTO.builder()
                .uSeq(deleteUser.getUSeq())
                .nickname(deleteUser.getNickname())
                .email(deleteUser.getEmail())
                .img(deleteUser.getImg())
                .build();
            return ResponseEntity.ok(resDeleteUser);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }

    }

//    @Operation(summary = "스터디그룹 멤버 추가(신청 수락)", description = "스터디그룹 멤버 추가 API 입니다.")
//    @PostMapping()
//    public ResponseEntity<?> plusMember(@AuthenticationPrincipal String uSeq,
//                                        @RequestBody GroupMembersDTO groupMembersDTO) {
//        try {
//            GroupMembersEntity groupMember = groupMembersService.createMember(uSeq, groupMembersDTO.getSgSeq(), groupMembersDTO.getUSeq());
//            return ResponseEntity.ok(groupMember);
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
//                .error(e.getMessage())
//                .build()
//            );
//        }
//
//    }

    @Operation(summary = "스터디그룹 탈퇴", description = "스터디그룹 탈퇴 API 입니다.")
    @DeleteMapping()
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal String uSeq, @RequestBody GroupMembersDTO groupMembersDTO) {
        try {
            UserEntity deleteUser = groupMembersService.deleteMember(uSeq, groupMembersDTO.getSgSeq());
            UserDTO resDeleteUser = UserDTO.builder()
                .uSeq(deleteUser.getUSeq())
                .nickname(deleteUser.getNickname())
                .email(deleteUser.getEmail())
                .img(deleteUser.getImg())
                .build();
            return ResponseEntity.ok(resDeleteUser);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }
}
