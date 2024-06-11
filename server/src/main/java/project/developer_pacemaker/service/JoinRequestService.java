package project.developer_pacemaker.service;

import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.joinRequest.JoinRequestControlDTO;
import project.developer_pacemaker.entity.JoinRequestEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.JoinRequestRepository;
import project.developer_pacemaker.repository.StudyGroupRepository;
import project.developer_pacemaker.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JoinRequestService {

    final private JoinRequestRepository joinRequestRepository;
    final private StudyGroupRepository studyGroupRepository;
    final private UserRepository userRepository;

    public JoinRequestService(JoinRequestRepository joinRequestRepository, StudyGroupRepository studyGroupRepository, UserRepository userRepository) {
        this.joinRequestRepository = joinRequestRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.userRepository = userRepository;
    }

    public List<Map<String, Object>> getJoinRequestBysgSeq(Long uSeq, long sgSeq){
        StudyGroupEntity studyGroupEntity = studyGroupRepository.findById(sgSeq)
                .orElseThrow(() -> new IllegalArgumentException("Study group not found"));

        if(studyGroupEntity.getUser().getUSeq()!=uSeq || studyGroupEntity.isDeleted()){
            return null;
        }
        List<JoinRequestEntity> results = studyGroupEntity.getJoinRequestEntityList();
        List<Map<String, Object>> joinRequests = new ArrayList<>();

        for (JoinRequestEntity result : results) {
            Map<String, Object> joinRequestMap = new HashMap<>();
            joinRequestMap.put("joinRequest", result);
            UserEntity user = userRepository.findById(result.getUSeq())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            joinRequestMap.put("nickname", user.getNickname());

            joinRequests.add(joinRequestMap);
        }
        return joinRequests;
    }

    public boolean postJoinRequest(Long uSeq, JoinRequestControlDTO joinRequestControlDTO ) {
        try {
            long sgSeq = joinRequestControlDTO.getSgSeq();
            StudyGroupEntity studyGroupEntity = studyGroupRepository.findById(sgSeq)
                    .orElseThrow(() -> new IllegalArgumentException("Study group not found"));

            if(studyGroupEntity.isDeleted() || studyGroupEntity.getUser().getUSeq()==uSeq){
                return false;
            }
            JoinRequestEntity joinRequestEntity = new JoinRequestEntity();
            joinRequestEntity.setUSeq(uSeq);
            joinRequestEntity.setStudyGroup(studyGroupEntity);
            joinRequestRepository.save(joinRequestEntity);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean deleteByjSeq(long uSeq, long jSeq) {
        try {
            JoinRequestEntity joinRequestEntity = joinRequestRepository.findById(jSeq)
                    .orElseThrow(() -> new RuntimeException(" join request with jSeq " + jSeq + " not found"));

            if(joinRequestEntity.getStudyGroup().getUser().getUSeq()==uSeq){ // 그룹장만 삭제 가능
                joinRequestRepository.delete(joinRequestEntity);
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
}
