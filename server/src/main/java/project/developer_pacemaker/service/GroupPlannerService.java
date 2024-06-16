package project.developer_pacemaker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.developer_pacemaker.dto.groupPlanner.GroupPlannerCreateDTO;
import project.developer_pacemaker.dto.groupPlanner.GroupTodoCreateDTO;
import project.developer_pacemaker.dto.groupPlanner.GroupTodoDTO;
import project.developer_pacemaker.entity.GroupPlannerEntity;
import project.developer_pacemaker.entity.GroupTodoEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.GroupPlannerRepository;
import project.developer_pacemaker.repository.GroupTodoRepository;
import project.developer_pacemaker.repository.StudyGroupRepository;
import project.developer_pacemaker.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupPlannerService {

    final private GroupPlannerRepository groupPlannerRepository;
    final private GroupTodoRepository groupTodoRepository;
    final private UserRepository userRepository;
    final private StudyGroupRepository studyGroupRepository;

    public GroupPlannerService(GroupPlannerRepository groupPlannerRepository, GroupTodoRepository groupTodoRepository, UserRepository userRepository, StudyGroupRepository studyGroupRepository) {
        this.groupPlannerRepository = groupPlannerRepository;
        this.groupTodoRepository = groupTodoRepository;
        this.userRepository = userRepository;
        this.studyGroupRepository = studyGroupRepository;
    }

    public List<GroupTodoDTO> getPlannerByDate(long sgSeq, long uSeq, String date) {
        try{
            String cleanedDate = date.trim().replaceAll("[^\\d-]", "");
            LocalDate parsedDate = LocalDate.parse(cleanedDate);

            Optional< GroupPlannerEntity> groupPlannerEntityOptional = groupPlannerRepository.findByUser_uSeqAndGpSeqAndIsDeletedAndRegistered(sgSeq, uSeq, false, parsedDate);
            if(groupPlannerEntityOptional.isPresent()){
                GroupPlannerEntity groupPlannerEntity = groupPlannerEntityOptional.get();
                List<GroupTodoEntity> groupTodoEntityList =groupTodoRepository.findByGroupPlanner(groupPlannerEntity);

                return groupTodoEntityList.stream()
                        .map(todo -> new GroupTodoDTO(todo.getGtSeq(), todo.getContent(), todo.isCompleted()))
                        .collect(Collectors.toList());
            }
            return null;
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return null;
        }
    }

    @Transactional
    public void saveGroupPlanner(Long uSeq, GroupPlannerCreateDTO groupPlanner) {
        try {
            UserEntity userEntity = userRepository.findById(uSeq)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            StudyGroupEntity studyGroupEntity = studyGroupRepository.findById(groupPlanner.getSgSeq())
                    .orElseThrow(() -> new IllegalArgumentException("Study Group not found"));

            GroupPlannerEntity groupPlannerEntity = new GroupPlannerEntity();
            groupPlannerEntity.setStudyGroup(studyGroupEntity);
            groupPlannerEntity.setUser(userEntity);
            groupPlannerRepository.save(groupPlannerEntity);

            List<GroupTodoCreateDTO> groupTodoCreateDTOList = groupPlanner.getGroupTodoCreateDTOList();

            if (groupTodoCreateDTOList != null && !groupTodoCreateDTOList.isEmpty()) {
                for (GroupTodoCreateDTO groupTodoDTO : groupTodoCreateDTOList) {
                    GroupTodoEntity groupTodoEntity = new GroupTodoEntity();
                    groupTodoEntity.setGroupPlanner(groupPlannerEntity);
                    groupTodoEntity.setContent(groupTodoDTO.getContent());
                    groupTodoEntity.setCompleted(groupTodoDTO.getIsCompleted() != null ? groupTodoDTO.getIsCompleted() : false);
                    groupTodoRepository.save(groupTodoEntity);
                }
            }
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
        }
    }

    public boolean deleteGroupPlannerBygpSeq(Long uSeq, long gpSeq) {
        try{
            GroupPlannerEntity groupPlannerEntity = groupPlannerRepository.findById(gpSeq)
                    .orElseThrow(() -> new RuntimeException("Group Planner data with gpSeq " + gpSeq + " not found"));

            // 본인이 작성한 플래너만 삭제 가능
            if(groupPlannerEntity.getUser().getUSeq() !=uSeq){
                return false;
            }
            groupPlannerEntity.setDeleted(true);
            groupPlannerRepository.save(groupPlannerEntity);

            List<GroupTodoEntity> groupTodoEntities = groupTodoRepository.findByGroupPlanner(groupPlannerEntity);

            if(groupTodoEntities != null && !groupTodoEntities.isEmpty()){
                for(GroupTodoEntity groupTodoEntity:groupTodoEntities){
                    groupTodoRepository.delete(groupTodoEntity);
                }
            }
            return true;
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return false;
        }
    }
}
