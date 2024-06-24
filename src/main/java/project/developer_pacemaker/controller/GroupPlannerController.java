package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.groupPlanner.GroupPlannerControllDTO;
import project.developer_pacemaker.dto.groupPlanner.GroupPlannerCreateDTO;
import project.developer_pacemaker.dto.groupPlanner.GroupPlannerRequestDTO;
import project.developer_pacemaker.dto.groupPlanner.GroupTodoDTO;
import project.developer_pacemaker.entity.GroupPlannerEntity;
import project.developer_pacemaker.entity.GroupTodoEntity;
import project.developer_pacemaker.service.GroupMembersService;
import project.developer_pacemaker.service.GroupPlannerService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group-planner")
public class GroupPlannerController {

    private final GroupPlannerService groupPlannerService;
    private final GroupMembersService groupMembersService;

    @Autowired
    public GroupPlannerController(GroupPlannerService groupPlannerService, GroupMembersService groupMembersService) {
        this.groupPlannerService = groupPlannerService;
        this.groupMembersService = groupMembersService;
    }

    @Operation(summary = "스터디그룹원 일자별 플래너 조회", description = "스터디그룹원 일자별 플래너 조회 API 입니다.")
    @PostMapping()
    public ResponseEntity<?> getGroupPlanner(@AuthenticationPrincipal String uSeq, @RequestParam String date, @RequestBody GroupPlannerRequestDTO groupPlannerRequestDTO){
        try{
            Long  uSeqLong = Long.parseLong(uSeq);
            // 해당 스터디 그룹에 포함된 유저인지 확인
            Boolean check1 = groupMembersService.checkGroupMember(groupPlannerRequestDTO.getUSeq(), groupPlannerRequestDTO.getSgSeq());
            Boolean check2 = groupMembersService.checkGroupMember(uSeqLong, groupPlannerRequestDTO.getSgSeq());
            if(!check1 || !check2){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load planner data");
            }

            Map<Long, List<GroupPlannerControllDTO>> planner = groupPlannerService.getPlannerByDate(groupPlannerRequestDTO.getSgSeq(),groupPlannerRequestDTO.getUSeq(), date);

            return new ResponseEntity<>(planner, HttpStatus.OK);
        }catch (Exception e){
            System.out.println("====e::"+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load planner data");
        }
    }

    @Operation(summary = "스터디그룹 플래너 작성", description = "스터디그룹 플래너 작성 API 입니다.")
    @PostMapping("/save")
    public ResponseEntity<?> saveGroupPlanner(@AuthenticationPrincipal String uSeq, @RequestParam String date, @RequestBody GroupPlannerCreateDTO groupPlanner){
        try{
            System.out.println("=========스터디그룹 플래너 작성=========");
            Long uSeqLong = Long.parseLong(uSeq);

            // 해당 스터디 그룹에 포함된 유저인지 확인
            Boolean check = groupMembersService.checkGroupMember(uSeqLong, groupPlanner.getSgSeq());
            if(!check){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save planner data");
            }

            String cleanedDate = date.trim().replaceAll("[^\\d-]", "");
            LocalDate parsedDate = LocalDate.parse(cleanedDate);

            GroupTodoEntity newPlanner = groupPlannerService.saveGroupPlanner(uSeqLong, groupPlanner, parsedDate);
            if(newPlanner !=null){
                return new ResponseEntity<>(newPlanner, HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save planner data");
            }
        }catch (Exception e){
            System.out.println("save::"+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save planner data");
        }
    }

    @Operation(summary = "스터디그룹 플래너 삭제", description = "스터디그룹 플래너 삭제 API 입니다.")
    @PatchMapping("/delete/{gpSeq}")
    public ResponseEntity<String> deleteGroupPlanner(@AuthenticationPrincipal String uSeq, @PathVariable long gpSeq){
        Long uSeqLong = Long.parseLong(uSeq);

        boolean deleted = groupPlannerService.deleteGroupPlannerBygpSeq(uSeqLong, gpSeq);
        if(deleted){
            return ResponseEntity.ok("Deletion successful");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete planner data");
        }
    }

    @Operation(summary = "스터디그룹원 월별 잔디 조회", description = "스터디그룹원 월별 잔디 조회 API 입니다.")
    @PostMapping("/grass")
    public ResponseEntity<?> getUserGroupGrass(@AuthenticationPrincipal String uSeq, @RequestParam String yearMonthStr, @RequestBody GroupPlannerRequestDTO groupPlannerRequestDTO){
        try{
            Long uSeqLong = Long.parseLong(uSeq);

            // 해당 스터디 그룹에 포함된 유저인지 확인
            Boolean check1 = groupMembersService.checkGroupMember(groupPlannerRequestDTO.getUSeq(), groupPlannerRequestDTO.getSgSeq());
            Boolean check2 = groupMembersService.checkGroupMember(uSeqLong, groupPlannerRequestDTO.getSgSeq());
            if(!check1 || !check2){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load user grass data");
            }

            YearMonth yearMonth = YearMonth.parse(yearMonthStr);

            Map<LocalDate, Long> grass = groupPlannerService.getGroupGrass(groupPlannerRequestDTO, yearMonth);

            return new ResponseEntity<>(grass, HttpStatus.OK);

        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load user grass data");
        }
    }


}
