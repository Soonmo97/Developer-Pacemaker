package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.RecruitmentBoardCreateDTO;
import project.developer_pacemaker.dto.RecruitmentBoardDTO;
import project.developer_pacemaker.dto.ResErrorDTO;
import project.developer_pacemaker.dto.StudyGroupDTO;
import project.developer_pacemaker.entity.RecruitmentBoardEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.service.RecruitmentBoardService;
import project.developer_pacemaker.service.StudyGroupService;

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

    @Operation(summary = "스터디 모집 게시글 작성", description = "스터디 모집 게시판 작성 API 입니다. <br> {sg_seq}, {content}, {name} / {sg_seq}는 스터디 그룹의 기본키입니다.(체크박스나 리스트로 선택)")
    @PostMapping
    public ResponseEntity<?> createRecruitmentBoard(@AuthenticationPrincipal String uSeq, @RequestBody RecruitmentBoardDTO recruitmentBoardDTO) {
        try {
            RecruitmentBoardCreateDTO createdBoard = recruitmentBoardService.createRecruitmentBoard(recruitmentBoardDTO, Long.parseLong(uSeq));
            return ResponseEntity.ok(createdBoard);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                    .error(e.getMessage())
                    .build()
            );
        }
    }

    @Operation(summary = "스터디 모집 게시글 조회", description = "스터디 모집 게시판 조회 API 입니다.")
    @GetMapping
    public List<RecruitmentBoardEntity> getAllRecruitmentBoards() {
        return recruitmentBoardService.getAllRecruitmentBoards();
    }

    @Operation(summary = "스터디 모집 게시글 제목 검색", description = "스터디 모집 게시판 제목 검색 API 입니다. <br> {name}")
    @GetMapping("/search")
    public ResponseEntity<List<RecruitmentBoardEntity>> getRecruitmentBoardByName(@RequestParam String name) {
        List<RecruitmentBoardEntity> recruitmentBoard = recruitmentBoardService.getRecruitmentBoardByName(name);
        if (recruitmentBoard.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(recruitmentBoard);
        }
    }

    @Operation(summary = "스터디 모집 게시글 수정", description = "스터디 모집 게시글 수정 API 입니다. <br> {rb_deq}, {content}, {name} <br> re_seq는 jwt 토큰에서")
    @PatchMapping("/{rb_seq}")
    public ResponseEntity<RecruitmentBoardEntity> updateRecruitmentBoard(
            @AuthenticationPrincipal String uSeq,
            @PathVariable Long rb_seq,  // @PathVariable은 URL에서 {id} 에 해당하는 부분 추출
            @RequestBody RecruitmentBoardDTO recruitmentBoardDetails) {
        return ResponseEntity.ok(recruitmentBoardService.updateRecruitmentBoard(rb_seq, recruitmentBoardDetails, uSeq));
    }

    @Operation(summary = "스터디 모집 게시글 삭제", description = "스터디 모집 게시글 삭제 API 입니다. <br> {uSeq}")
    @DeleteMapping("/{rb_seq}")
    public ResponseEntity<Void> deleteRecruitmentBoard(@PathVariable Long rb_seq, @RequestParam String uSeq) {
        recruitmentBoardService.deleteRecruitmentBoard(rb_seq, uSeq);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내가 그룹장인 스터디 그룹 리스트", description = "내가 그룹장인 스터디 그룹 리스트 API 입니다. 이미 스터디 모집 게시글이 있는 스터디 그룹은 제외되고 표시됩니다.")
    @GetMapping("/myStudyGroups")
    public ResponseEntity<List<StudyGroupDTO>> getMyStudyGroups(@AuthenticationPrincipal String uSeq) {
        List<StudyGroupDTO> studyGroups = recruitmentBoardService.getMyStudyGroups(Long.parseLong(uSeq));
        if(studyGroups.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(studyGroups);
        }
    }
}