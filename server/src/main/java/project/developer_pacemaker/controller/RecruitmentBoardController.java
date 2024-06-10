package project.developer_pacemaker.controller;

import com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.entity.RecruitmentBoardEntity;
import project.developer_pacemaker.service.RecruitmentBoardService;

import java.util.List;

@Controller
@RequestMapping("/api/recruitmentBoard")
public class RecruitmentBoardController {

    @Autowired
    private RecruitmentBoardService recruitmentBoardService;

    @Operation(summary = "스터디 모집 게시판 작성", description = "스터디 모집 게시판 작성 API 입니다.")
    @PostMapping
    private RecruitmentBoardEntity createRecruitmentBoard(@RequestBody RecruitmentBoardEntity recruitmentBoard){
        return recruitmentBoardService.createRecruitmentBoard(recruitmentBoard);
    }
    @Operation(summary = "스터디 모집 게시판 조회", description = "스터디 모집 게시판 조회 API 입니다.")
    @GetMapping
    private List<RecruitmentBoardEntity> getAllRecruitmentBoards(){
        return recruitmentBoardService.getAllRecruitmentBoards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentBoardEntity> getRecruitmentBoardById(@PathVariable Long id){
        // ResponseEntity반환, 객체는 RecruitmentBoardEntity 본문으로, @PathVariable은 URL에서 {id} 에 해당하는 부분을 추출
        return recruitmentBoardService.getRecruitmentBoardById(id)
                // recruitmentBoardService의 getRecruitmentBoardById는 optional -> 값 있을수도, 없을수도
                .map(ResponseEntity::ok) // optional에 값 있으면 ok()후 ResponseEntity에 담음
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentBoardEntity> updateRecruitmentBoard(@PathVariable Long id, @RequestBody RecruitmentBoardEntity recruitmentBoardDetails, @RequestParam String uSeq){
        // @PathVariable은 URL에서 {id} 에 해당하는 부분을 추출
        return ResponseEntity.ok(recruitmentBoardService.updateRecruitmentBoard(id, recruitmentBoardDetails, uSeq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentBoard(@PathVariable Long id, @RequestParam String uSeq){
        recruitmentBoardService.deleteRecruitmentBoard(id, uSeq);
        return ResponseEntity.noContent().build();
    }
}
