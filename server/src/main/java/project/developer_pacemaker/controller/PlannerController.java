package project.developer_pacemaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.planner.PlannerCreateDTO;
import project.developer_pacemaker.dto.planner.PlannerDTO;
import project.developer_pacemaker.dto.planner.TodoDTO;
import project.developer_pacemaker.entity.PlannerEntity;
import project.developer_pacemaker.service.PlannerService;

import java.util.List;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {

    @Autowired
    PlannerService plannerService;

    @GetMapping()
    public ResponseEntity<?> getMyPlanner(@AuthenticationPrincipal String uSeq, @RequestParam String date){
        try{
            Long  uSeqLong = Long.parseLong(uSeq);
            List<TodoDTO> planner= plannerService.getPlannerByDate(uSeqLong, date);
            return new ResponseEntity<>(planner, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load planner data");
        }
    }

    @PostMapping()
    public ResponseEntity<String> savePlanner(@AuthenticationPrincipal String uSeq, @RequestBody PlannerCreateDTO planner){
        // 테스트 데이터 ("todoDTOList" 값 필수 -> [] 빈 배열이라도 보낼 것)
        // todoDTOList 안의 content만 필수(나머지는 선택)
        // 예시
//         {
//          "todoDTOList": [
//            {
//              "content": "Study Java3",
//              "duration":3,
//              "isCompleted": true
//            },
//            {
//              "content": "Read Spring Boot documentation3"
//            }
//          ]
//        }
        try{
            Long uSeqLong = Long.parseLong(uSeq);
            plannerService.savePlanner(uSeqLong, planner);
            return new ResponseEntity<>("Your planner saved successfully", HttpStatus.CREATED);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save planner data");
        }
    }

    @PatchMapping("/delete/{pSeq}")
    public ResponseEntity<String> deletePlanner(@AuthenticationPrincipal String uSeq, @PathVariable long pSeq){
        Long uSeqLong = Long.parseLong(uSeq);
        boolean deleted = plannerService.deletePlannerBypSeq(uSeqLong, pSeq);

        if(deleted){
            return ResponseEntity.ok("Deletion successful");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete planner data");
        }
    }

}
