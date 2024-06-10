package project.developer_pacemaker.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.developer_pacemaker.entity.RecruitmentBoardEntity;
import project.developer_pacemaker.repository.RecruitmentBoardRepository;

import java.util.List;
import java.util.Optional;

@Service
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

    public RecruitmentBoardEntity updateRecruitmentBoard(Long id, RecruitmentBoardEntity recruitmentBoardDetails){
        Optional<RecruitmentBoardEntity> optionalRecruitmentBoard = recruitmentBoardRepository.findById(id);
        if(optionalRecruitmentBoard.isPresent()){ // 값이 있으면 true, 없으면 false
            RecruitmentBoardEntity recruitmentBoard = optionalRecruitmentBoard.get(); // 값이 있으면
            recruitmentBoard.setContent(recruitmentBoardDetails.getContent());
            return recruitmentBoardRepository.save(recruitmentBoard);
        }else{
            throw new RuntimeException("RecruitmentBoard id를 찾을 수 없습니다."+id);
        }
    }


}
