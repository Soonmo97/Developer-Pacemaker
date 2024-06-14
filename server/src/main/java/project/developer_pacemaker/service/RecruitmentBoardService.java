package project.developer_pacemaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.RecruitmentBoardDTO;
import project.developer_pacemaker.entity.RecruitmentBoardEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.repository.RecruitmentBoardRepository;
import project.developer_pacemaker.repository.StudyGroupRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RecruitmentBoardService {
    private final RecruitmentBoardRepository recruitmentBoardRepository;
    private final StudyGroupRepository studyGroupRepository;
    @Autowired
    public RecruitmentBoardService(RecruitmentBoardRepository recruitmentBoardRepository, StudyGroupRepository studyGroupRepository) {
        this.recruitmentBoardRepository = recruitmentBoardRepository;
        this.studyGroupRepository = studyGroupRepository;
    }
    public RecruitmentBoardEntity createRecruitmentBoard(RecruitmentBoardDTO recruitmentBoardDTO, long uSeq) {
        // StudyGroupEntity를 이용하여 name을 설정
//        StudyGroupEntity studyGroup = recruitmentBoard.getSgSeq();
//        if (studyGroup != null) {
            StudyGroupEntity foundStudyGroup = studyGroupRepository.findById(recruitmentBoardDTO.getSgSeq())
                    .orElseThrow(()-> new RuntimeException("스터디 그룹을 찾을 수 없습니다."));

            log.info("studyGroup sgSeq: {}", foundStudyGroup.getSgSeq());
            if(foundStudyGroup.getUser().getUSeq() != uSeq){
                throw new RuntimeException("사용자가 스터디 그룹장이 아닙니다.");
            }
//                recruitmentBoard.setTitle(optionalStudyGroup.get().getName())
                RecruitmentBoardEntity recruitmentBoard = RecruitmentBoardEntity.builder()
                .studyGroup(foundStudyGroup)
                .content(recruitmentBoardDTO.getContent())
                .name(recruitmentBoardDTO.getName())
                .build();
        return recruitmentBoardRepository.save(recruitmentBoard);
    }

    public List<RecruitmentBoardEntity> getAllRecruitmentBoards(){
        return recruitmentBoardRepository.findAll();
    }

    public List<RecruitmentBoardEntity> getRecruitmentBoardByName(String name){
        return recruitmentBoardRepository.findByName(name);
    }

    public RecruitmentBoardEntity updateRecruitmentBoard(Long id, RecruitmentBoardDTO recruitmentBoardDetails, String uSeq){
        Optional<RecruitmentBoardEntity> optionalRecruitmentBoard = recruitmentBoardRepository.findById(id);
        if(optionalRecruitmentBoard.isPresent()){ // 값이 있으면 true, 없으면 false
            RecruitmentBoardEntity recruitmentBoard = optionalRecruitmentBoard.get(); // 값이 있으면
            StudyGroupEntity studyGroup = recruitmentBoard.getStudyGroup();
            // 스터디 그룹장이 작성자인지
            if(studyGroup != null && Long.parseLong(uSeq) != studyGroup.getUser().getUSeq()){
                log.warn("스터디 그룹장이 아닙니다. {}", uSeq);
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
