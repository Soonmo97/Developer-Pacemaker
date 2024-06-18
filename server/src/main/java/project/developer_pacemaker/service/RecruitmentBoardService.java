package project.developer_pacemaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.RecruitmentBoardCreateDTO;
import project.developer_pacemaker.dto.RecruitmentBoardDTO;
import project.developer_pacemaker.dto.StudyGroupDTO;
import project.developer_pacemaker.entity.RecruitmentBoardEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.RecruitmentBoardRepository;
import project.developer_pacemaker.repository.StudyGroupRepository;
import lombok.extern.slf4j.Slf4j;
import project.developer_pacemaker.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecruitmentBoardService {
    private final RecruitmentBoardRepository recruitmentBoardRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    @Autowired
    public RecruitmentBoardService(RecruitmentBoardRepository recruitmentBoardRepository, StudyGroupRepository studyGroupRepository, UserRepository userRepository) {
        this.recruitmentBoardRepository = recruitmentBoardRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.userRepository = userRepository;
    }
    public RecruitmentBoardCreateDTO createRecruitmentBoard(RecruitmentBoardDTO recruitmentBoardDTO, long uSeq) {
            StudyGroupEntity foundStudyGroup = studyGroupRepository.findById(recruitmentBoardDTO.getSgSeq())
                    .orElseThrow(()-> new RuntimeException("스터디 그룹을 찾을 수 없습니다."));
//            UserEntity user = userRepository.findById(foundStudyGroup.getUser().getUSeq())
//                    .orElseThrow(()-> new RuntimeException("user 없음."));
        log.info("studyGroup sgSeq: {}", foundStudyGroup.getSgSeq());
            if(foundStudyGroup.getUser().getUSeq() != uSeq){
                throw new RuntimeException("사용자가 스터디 그룹장이 아닙니다.");
            }
                RecruitmentBoardEntity recruitmentBoard = RecruitmentBoardEntity.builder()
                .studyGroup(foundStudyGroup)
                .content(recruitmentBoardDTO.getContent())
                .name(recruitmentBoardDTO.getName())
                .build();
            recruitmentBoardRepository.save(recruitmentBoard);
            // 모집여부 1(모집 중)으로 변경
            foundStudyGroup.setStatus(true);
            studyGroupRepository.save(foundStudyGroup);


            return RecruitmentBoardCreateDTO.builder()
                    .studyGroup(foundStudyGroup)
                    .content(recruitmentBoardDTO.getContent())
                    .name(recruitmentBoardDTO.getName())
                    .nickname(foundStudyGroup.getUser().getNickname())
                    .build();
    }


    public RecruitmentBoardEntity updateRecruitmentBoard(Long id, RecruitmentBoardDTO recruitmentBoardDetails, String uSeq){
        Optional<RecruitmentBoardEntity> optionalRecruitmentBoard = recruitmentBoardRepository.findById(id);
        if(optionalRecruitmentBoard.isPresent()){ // 값이 있으면 true, 없으면 false
            RecruitmentBoardEntity recruitmentBoard = optionalRecruitmentBoard.get(); // 값이 있으면
            StudyGroupEntity studyGroup = recruitmentBoard.getStudyGroup();
            // 스터디 그룹장이 작성자인지
            if(studyGroup != null && Long.parseLong(uSeq) != studyGroup.getUser().getUSeq()){
                log.warn("you are not study group leader. {}", uSeq);
                throw new RuntimeException("you are not leader of study group.");
            }
            // if문 통과하면 게시글 업데이트
            recruitmentBoard.setContent(recruitmentBoardDetails.getContent());
            recruitmentBoard.setName(recruitmentBoardDetails.getName());
            return recruitmentBoardRepository.save(recruitmentBoard);
        }else{
            throw new RuntimeException("not found RecruitmentBoard id."+id);
        }
    }

    public List<RecruitmentBoardCreateDTO> getAllRecruitmentBoards(){
        List<RecruitmentBoardEntity> recruitmentBoardEntities = recruitmentBoardRepository.findAll();
        List<RecruitmentBoardCreateDTO> recruitmentBoardCreates = new ArrayList<>();
        for(RecruitmentBoardEntity item : recruitmentBoardEntities) {
            recruitmentBoardCreates.add(RecruitmentBoardCreateDTO.builder()
                    .rbSeq(item.getRbSeq())
                    .registered(item.getRegistered())
                    .content(item.getContent())
                    .nickname(item.getStudyGroup().getUser().getNickname())
                    .name(item.getName())
                    .studyGroup(item.getStudyGroup())
                    .build());
        }
        return recruitmentBoardCreates;
    }

    public List<RecruitmentBoardEntity> getRecruitmentBoardByName(String name){
        return recruitmentBoardRepository.findByName(name);
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
            studyGroup.setStatus(false);
            studyGroupRepository.save(studyGroup);
        } else {
            throw new RuntimeException("스터디 모집 게시글 id를 찾을 수 없습니다."+id);
        }
    }
    public List<StudyGroupDTO> getMyStudyGroups(Long uSeq) {
        UserEntity user = userRepository.findById(uSeq)
                .orElseThrow(() -> new RuntimeException("User not found with id " + uSeq));
        List<Long> recruitmentBoardSgSeq = recruitmentBoardRepository.findAllSgSeqInRecruitmentBoard();
        return studyGroupRepository.findByUser(user).stream()
                .filter(studyGroup -> !recruitmentBoardSgSeq.contains(studyGroup.getSgSeq()))
                .map(studyGroup -> StudyGroupDTO.builder()
                        .sgSeq(studyGroup.getSgSeq())
                        .name(studyGroup.getName())
                        .uSeq(studyGroup.getUser().getUSeq())
                        .registered(studyGroup.getRegistered())
                        .goal(studyGroup.getGoal())
                        .status(studyGroup.isStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
