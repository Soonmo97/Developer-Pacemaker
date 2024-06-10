package project.developer_pacemaker.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.developer_pacemaker.entity.RecruitmentBoardEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.repository.RecruitmentBoardRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RecruitmentBoardService {
    @Autowired
    private RecruitmentBoardRepository recruitmentBoardRepository;

    public RecruitmentBoardEntity createRecruitmentBoard(RecruitmentBoardEntity recruitmentBoard) {
        return recruitmentBoardRepository.save(recruitmentBoard);
    }
    public List<RecruitmentBoardEntity> getAllRecruitmentBoards(){
        return recruitmentBoardRepository.findAll();
    }
    public Optional<RecruitmentBoardEntity> getRecruitmentBoardById(Long id){ // 값이 있을 수도, 없을수도 = optional
        return recruitmentBoardRepository.findById(id); //
    }

    public RecruitmentBoardEntity updateRecruitmentBoard(Long id, RecruitmentBoardEntity recruitmentBoardDetails, String u_Seq){
        Optional<RecruitmentBoardEntity> optionalRecruitmentBoard = recruitmentBoardRepository.findById(id);
        if(optionalRecruitmentBoard.isPresent()){ // 값이 있으면 true, 없으면 false
            RecruitmentBoardEntity recruitmentBoard = optionalRecruitmentBoard.get(); // 값이 있으면
            StudyGroupEntity studyGroup = recruitmentBoard.getStudyGroup();
            // 스터디 그룹장이 작성자인지
            if(studyGroup != null && Long.parseLong(u_Seq) != studyGroup.getUser().getUSeq()){
                log.warn("스터디 그룹장이 아닙니다. {}", u_Seq);
                throw new RuntimeException("스터디 그룹의 그룹장이 아닙니다.");
            }
            // if문 통과하면 게시글 업데이트
            recruitmentBoard.setContent(recruitmentBoardDetails.getContent());
            return recruitmentBoardRepository.save(recruitmentBoard);
        }else{
            throw new RuntimeException("RecruitmentBoard id를 찾을 수 없습니다."+id);
        }
    }
    public void deleteRecruitmentBoard(Long id, String u_Seq){
        Optional<RecruitmentBoardEntity> optionalRecruitmentBoard = recruitmentBoardRepository.findById(id);
        if(optionalRecruitmentBoard.isPresent()){
            RecruitmentBoardEntity recruitmentBoard= optionalRecruitmentBoard.get(); // 값 있으면
            StudyGroupEntity studyGroup = recruitmentBoard.getStudyGroup();
            if(studyGroup != null && Long.parseLong(u_Seq) != studyGroup.getUser().getUSeq()){
                log.warn("스터디 그룹장이 아닙니다. {}", u_Seq);
                throw new RuntimeException("스터디 그룹의 그룹장이 아닙니다.");
            }
            recruitmentBoardRepository.deleteById(id);
        } else {
            throw new RuntimeException("스터디 모집 게시글 id를 찾을 수 없습니다."+id);
        }
    }
}
