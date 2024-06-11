package project.developer_pacemaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.joinRequest.JoinRequestControlDTO;
import project.developer_pacemaker.service.JoinRequestService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/join")
public class JoinRequestController {
    @Autowired
    JoinRequestService joinRequestService;

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

    @PostMapping("/accept/{jSeq}")
    public ResponseEntity<String> acceptJoinRequest(@AuthenticationPrincipal String uSeq, @RequestBody JoinRequestControlDTO joinRequestControlDTO, @PathVariable long jSeq){
        try{
            Long uSeqLong = Long.parseLong(uSeq);

            // 1. 스터디그룹 인원 만들어지면 작성 예정) 스터디그룹인원에 데이터 추가 (해당 스터디에 이미 참여된 유저인지 확인할 것)

            // 2. 알림 테이블에서 해당 데이터 삭제
            boolean delete = joinRequestService.deleteByjSeq(uSeqLong, jSeq);
            if(delete){
                return new ResponseEntity<>("join request deleted successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to accept join request");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to accept join request");
        }
    }

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
