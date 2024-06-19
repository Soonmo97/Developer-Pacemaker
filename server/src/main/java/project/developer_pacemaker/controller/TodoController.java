package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.planner.TodoCreateDTO;
import project.developer_pacemaker.dto.planner.TodoDTO;
import project.developer_pacemaker.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {
    @Autowired
    TodoService todoService;

    @Operation(summary = "특정 플래너 투두 조회(개인)", description = "특정 플래너 투두 조회(개인) API 입니다.")
    @GetMapping("/{pSeq}")
    public ResponseEntity<?> getTodoList(@AuthenticationPrincipal String uSeq, @PathVariable long pSeq){
        try{
            Long uSeqLong = Long.parseLong(uSeq);
            List<TodoDTO> todoDTOList = todoService.getTodoBypSeq(uSeqLong, pSeq);
            return new ResponseEntity<>(todoDTOList, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load todo list");
        }
    }

    @Operation(summary = "개인 투두 생성", description = "개인 투두 생성 API 입니다.")
    @PostMapping("/{pSeq}")
    public ResponseEntity<String> saveTodo(@AuthenticationPrincipal String uSeq, @PathVariable long pSeq, @RequestBody TodoCreateDTO todo){
        try {
            Long uSeqLong = Long.parseLong(uSeq);

            boolean save = todoService.saveTodo(uSeqLong, pSeq, todo );
            if(save){
                return new ResponseEntity<>("Your todo saved successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save todo data");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save todo data");
        }
    }

    @Operation(summary = "개인 투두 수정", description = "개인 투두 수정 API 입니다.")
    @PatchMapping("/{tSeq}")
    public ResponseEntity<String> updateTodo(@AuthenticationPrincipal String uSeq, @PathVariable long tSeq, @RequestBody TodoDTO todo){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            boolean update = todoService.updateTodo(uSeqLong, tSeq, todo);
            if(update){
                return new ResponseEntity<>("Your todo updated successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update todo data");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update todo data");
        }
    }

    @Operation(summary = "개인 투두 삭제", description = "개인 투두 삭제 API 입니다.")
    @DeleteMapping("/{tSeq}")
    public ResponseEntity<String> deleteTodo(@AuthenticationPrincipal String uSeq, @PathVariable long tSeq){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            boolean delete = todoService.deleteTodo(uSeqLong, tSeq);
            if(delete){
                return new ResponseEntity<>("Your todo deleted successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete todo data");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete todo data");
        }
    }

    @Operation(summary = "개인 투두 완료/미완료 변경", description = "개인 투두 완료/미완료 변경 API 입니다.")
    @PatchMapping("change/{tSeq}")
    public ResponseEntity<String> patchComplete(@AuthenticationPrincipal String uSeq, @PathVariable long tSeq){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            boolean update = todoService.patchComplete(uSeqLong, tSeq);
            if(update){
                return new ResponseEntity<>("Your todo updated successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update todo data");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update todo data");
        }
    }
}
