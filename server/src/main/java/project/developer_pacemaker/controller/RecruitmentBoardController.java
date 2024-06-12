package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.RecruitmentBoardDTO;
import project.developer_pacemaker.entity.RecruitmentBoardEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.service.RecruitmentBoardService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/recruitmentBoard")
public class RecruitmentBoardController {

    final private RecruitmentBoardService recruitmentBoardService;
    @Autowired
    public RecruitmentBoardController(RecruitmentBoardService recruitmentBoardService) {
        this.recruitmentBoardService = recruitmentBoardService;
    }

    @Operation(summary = "스터디 모집 게시글 작성", description = "스터디 모집 게시판 작성 API 입니다.")
    @PostMapping
    public ResponseEntity<RecruitmentBoardEntity> createRecruitmentBoard(@RequestBody RecruitmentBoardDTO recruitmentBoardDTO) {
        log.info("DTO: {}", recruitmentBoardDTO);  // DTO 값 로그 추가
        RecruitmentBoardEntity recruitmentBoard = RecruitmentBoardEntity.builder()
                .studyGroup(StudyGroupEntity.builder().sgSeq(recruitmentBoardDTO.getSg_seq()).build())
                .content(recruitmentBoardDTO.getContent())
                .title((recruitmentBoardDTO.getTitle()))
                .build();
        log.info("DTO: {}",recruitmentBoardDTO);
        try {
            RecruitmentBoardEntity createdBoard = recruitmentBoardService.createRecruitmentBoard(recruitmentBoard, recruitmentBoardDTO.getU_seq());
            return ResponseEntity.ok(createdBoard);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(null);
        }
    }
    @Operation(summary = "스터디 모집 게시글 조회", description = "스터디 모집 게시판 조회 API 입니다.")
    @GetMapping
    public List<RecruitmentBoardEntity> getAllRecruitmentBoards(){
        return recruitmentBoardService.getAllRecruitmentBoards();
    }
    @Operation(summary = "스터디 모집 게시글 제목 검색", description = "스터디 모집 게시판 제목 검색 API 입니다.")
    @GetMapping("/search")
    public ResponseEntity<List<RecruitmentBoardEntity>> getRecruitmentBoardByTitle(@RequestParam String title){
        List<RecruitmentBoardEntity> recruitmentBoars = recruitmentBoardService.getRecruitmentBoardByTitle(title);
        if(recruitmentBoars.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(recruitmentBoars);
        }
    }
    @Operation(summary = "스터디 모집 게시글 수정", description = "스터디 모집 게시글 수정 API 입니다.")
    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentBoardEntity> updateRecruitmentBoard(@PathVariable Long id, @RequestBody RecruitmentBoardEntity recruitmentBoardDetails, @RequestParam String uSeq){
        // @PathVariable은 URL에서 {id} 에 해당하는 부분을 추출
        return ResponseEntity.ok(recruitmentBoardService.updateRecruitmentBoard(id, recruitmentBoardDetails, uSeq));
    }

    @Operation(summary = "스터디 모집 게시글 삭제", description = "스터디 모집 게시글 삭제 API 입니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentBoard(@PathVariable Long id, @RequestParam String uSeq){
        recruitmentBoardService.deleteRecruitmentBoard(id, uSeq);
        return ResponseEntity.noContent().build();
    }
}
