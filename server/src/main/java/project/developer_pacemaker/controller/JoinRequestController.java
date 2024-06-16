package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.joinRequest.JoinRequestControlDTO;
import project.developer_pacemaker.service.GroupMembersService;
import project.developer_pacemaker.service.JoinRequestService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/join")
public class JoinRequestController {
    @Autowired
    JoinRequestService joinRequestService;

    @Autowired
    GroupMembersService groupMembersService;

    @Operation(summary = "가입 신청 리스트 조회", description = "가입 신청 리스트 조회 API 입니다.")
    @GetMapping("/{sgSeq}")
    public ResponseEntity<?> getJoinRequest(@AuthenticationPrincipal String uSeq, @PathVariable long sgSeq){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            List<Map<String, Object>> joinRequestDTOList = joinRequestService.getJoinRequestBysgSeq(uSeqLong, sgSeq);
            return new ResponseEntity<>(joinRequestDTOList, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load join request data");
        }
    }

    @Operation(summary = "가입 신청 생성", description = "가입 신청 생성 API 입니다.")
    @PostMapping()
    public ResponseEntity<String> postJoinRequest(@AuthenticationPrincipal String uSeq, @RequestBody JoinRequestControlDTO joinRequestControlDTO){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            boolean post = joinRequestService.postJoinRequest(uSeqLong, joinRequestControlDTO);
            if(post){
                return new ResponseEntity<>("Your join request was sent successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to send your join request");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to send your join request");
        }
    }

    @Operation(summary = "가입 신청 수락", description = "가입 신청 수락 API 입니다.")
    @PostMapping("/accept/{jSeq}")
    public ResponseEntity<String> acceptJoinRequest(@AuthenticationPrincipal String uSeq, @RequestBody JoinRequestControlDTO joinRequestControlDTO, @PathVariable long jSeq){
        try{
            Long uSeqLong = Long.parseLong(uSeq);

            // 1. 스터디그룹 인원 데이터 추가 (해당 스터디에 이미 참여된 유저인지 확인할 것)
            boolean create = groupMembersService.createMember(uSeqLong, joinRequestControlDTO.getSgSeq(), joinRequestControlDTO.getUSeq());
            if(!create){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to accept join request");
            }
            // 2. 알림 테이블에서 해당 데이터 삭제
            boolean delete = joinRequestService.deleteByjSeq(uSeqLong, jSeq);
            if(delete){
                return new ResponseEntity<>("Join request accepted successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to accept join request");
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to accept join request");
        }
    }

    @Operation(summary = "가입 신청 거절", description = "가입 신청 거절 API 입니다.")
    @DeleteMapping("/reject/{jSeq}")
    public ResponseEntity<String> rejectJoinRequest(@AuthenticationPrincipal String uSeq, @PathVariable long jSeq){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            boolean delete = joinRequestService.deleteByjSeq(uSeqLong,jSeq);
            if(delete){
                return new ResponseEntity<>("join request rejected successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to reject join request");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to reject join request");
        }
    }


}
