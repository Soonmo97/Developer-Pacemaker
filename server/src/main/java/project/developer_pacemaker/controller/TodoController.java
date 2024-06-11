package project.developer_pacemaker.controller;

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

    @PatchMapping("/{tSeq}")
    public ResponseEntity<String> updateTodo(@AuthenticationPrincipal String uSeq, @PathVariable long tSeq, @RequestBody TodoDTO todo){
        try {
            Long uSeqLong = Long.parseLong(uSeq);
            boolean save = todoService.updateTodo(uSeqLong, tSeq, todo );
            if(save){
                return new ResponseEntity<>("Your todo updated successfully", HttpStatus.CREATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update todo data");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update todo data");
        }
    }

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
}
