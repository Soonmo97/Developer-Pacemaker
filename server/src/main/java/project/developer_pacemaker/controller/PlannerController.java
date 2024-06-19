package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.planner.PlannerCreateDTO;
import project.developer_pacemaker.dto.planner.PlannerDTO;
import project.developer_pacemaker.service.PlannerService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {

    @Autowired
    PlannerService plannerService;
    @Operation(summary = "개인 플래너 일자별 조회", description = "개인 플래너 일자별 조회 API 입니다.")
    @GetMapping()
    public ResponseEntity<?> getMyPlanner(@AuthenticationPrincipal String uSeq, @RequestParam String date){
        try{
            Long  uSeqLong = Long.parseLong(uSeq);
            PlannerDTO planner= plannerService.getPlannerByDate(uSeqLong, date);
            return new ResponseEntity<>(planner, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load planner data");
        }
    }
    @Operation(summary = "개인 플래너 작성", description = "개인 플래너 작성 API 입니다.")
    @PostMapping()
    public ResponseEntity<String> savePlanner(@AuthenticationPrincipal String uSeq, @RequestParam String date, @RequestBody PlannerCreateDTO planner){
        try{
            Long uSeqLong = Long.parseLong(uSeq);
            boolean save = plannerService.savePlanner(uSeqLong, planner, date);

            if(save){
                return new ResponseEntity<>("Your planner saved successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save planner data");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save planner data");
        }
    }

    @Operation(summary = "개인 플래너 삭제", description = "개인 플래너 삭제 API 입니다.")
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

    @Operation(summary = "개인 플래너 수정", description = "개인 플래너 수정 API 입니다.")
    @PatchMapping("/patch/{pSeq}")
    public ResponseEntity<String> updatePlanner(@AuthenticationPrincipal String uSeq, @RequestBody PlannerCreateDTO planner, @PathVariable long pSeq){
        Long uSeqLong = Long.parseLong(uSeq);
        boolean update = plannerService.updatePlannerBypSeq(uSeqLong, pSeq, planner);

        if(update){
            return ResponseEntity.ok("Update successful");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to Update planner data");
        }
    }

    @Operation(summary = "월별 개인 잔디 조회", description = "월별 개인 잔디 조회 API 입니다.")
    @GetMapping("/grass")
    public ResponseEntity<?> getUserGroupGrass(@AuthenticationPrincipal String uSeq, @RequestParam String yearMonthStr){
        try{
            Long uSeqLong = Long.parseLong(uSeq);
            YearMonth yearMonth = YearMonth.parse(yearMonthStr);

            Map<LocalDate, Long> grass = plannerService.getUserGrass(uSeqLong, yearMonth);

            return new ResponseEntity<>(grass, HttpStatus.OK);
        }catch (Exception e){
            System.out.println("e:: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load user grass data");
        }
    }


}
