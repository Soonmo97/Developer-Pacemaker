package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.groupPlanner.GroupTodoCreateDTO;
import project.developer_pacemaker.entity.GroupTodoEntity;
import project.developer_pacemaker.service.GroupTodoService;

@RestController
@RequestMapping("/api/group-todo")
public class GroupTodoController {
    @Autowired
    GroupTodoService groupTodoService;

    @Operation(summary = "그룹 투두 생성", description = "그룹 투두 생성 API 입니다.")
    @PostMapping("/{gpSeq}")
    public ResponseEntity<?> saveTodo(@AuthenticationPrincipal String uSeq, @PathVariable long gpSeq, @RequestBody GroupTodoCreateDTO groupTodo){
        try {
            System.out.println("======groupTodo========"+groupTodo.getContent()+"    "+gpSeq);
            Long uSeqLong = Long.parseLong(uSeq);
            GroupTodoEntity newTodo = groupTodoService.saveGroupTodo(uSeqLong, gpSeq, groupTodo);

            if(newTodo != null){
                return new ResponseEntity<>(newTodo, HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save todo data");
            }
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save todo data");
        }
    }

    @Operation(summary = "그룹 투두 수정", description = "그룹 투두 수정 API 입니다.")
    @PatchMapping("/{gtSeq}")
    public ResponseEntity<String> updateTodo(@AuthenticationPrincipal String uSeq, @PathVariable long gtSeq, @RequestBody GroupTodoCreateDTO groupTodo){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            boolean update = groupTodoService.updateGroupTodo(uSeqLong, gtSeq, groupTodo);

            if(update){
                return new ResponseEntity<>("Your todo updated successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update todo data");
            }
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update todo data");
        }
    }

    @Operation(summary = "그룹 투두 삭제", description = "그룹 투두 삭제 API 입니다.")
    @DeleteMapping("/{gtSeq}")
    public ResponseEntity<String> deleteGroupTodo(@AuthenticationPrincipal String uSeq, @PathVariable long gtSeq){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            boolean delete = groupTodoService.deleteGroupTodo(uSeqLong, gtSeq);

            if(delete){
                return new ResponseEntity<>("Your todo deleted successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete todo data");
            }

        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete todo data");
        }
    }

    @Operation(summary = "그룹 투두 완료/미완료 변경", description = "그룹 투두 완료/미완료 변경 API 입니다.")
    @PatchMapping("change/{gtSeq}")
    public ResponseEntity<Boolean> patchTodoComplete(@AuthenticationPrincipal String uSeq, @PathVariable long gtSeq){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            boolean update = groupTodoService.patchComplete(uSeqLong, gtSeq);

                return new ResponseEntity<>(update, HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }
    }
}
