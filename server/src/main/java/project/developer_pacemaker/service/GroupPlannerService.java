package project.developer_pacemaker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.developer_pacemaker.dto.groupPlanner.*;
import project.developer_pacemaker.entity.GroupPlannerEntity;
import project.developer_pacemaker.entity.GroupTodoEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.GroupPlannerRepository;
import project.developer_pacemaker.repository.GroupTodoRepository;
import project.developer_pacemaker.repository.StudyGroupRepository;
import project.developer_pacemaker.repository.UserRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<Long, List<GroupPlannerControllDTO>> getPlannerByDate(long sgSeq, long uSeq, String date) {
        try{
            String cleanedDate = date.trim().replaceAll("[^\\d-]", "");
            LocalDate parsedDate = LocalDate.parse(cleanedDate);

            Optional< GroupPlannerEntity> groupPlannerEntityOptional = groupPlannerRepository.findByUser_uSeqAndStudyGroup_sgSeqAndIsDeletedAndRegistered(uSeq, sgSeq, false, parsedDate);
            if(groupPlannerEntityOptional.isPresent()){
                GroupPlannerEntity groupPlannerEntity = groupPlannerEntityOptional.get();
                List<GroupTodoEntity> groupTodoEntityList =groupTodoRepository.findByGroupPlanner(groupPlannerEntity);

                Map<Long, List<GroupPlannerControllDTO>> result = new HashMap<>();
                List<GroupPlannerControllDTO> groupPlannerControllDTOList =  groupTodoEntityList.stream()
                        .map(todo -> new GroupPlannerControllDTO(groupPlannerEntity.getGpSeq(),todo.getGtSeq(), todo.getContent(), todo.isCompleted()))
                        .collect(Collectors.toList());

                result.put(groupPlannerEntity.getGpSeq(), groupPlannerControllDTOList);

                return result;
            }
            return null;
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return null;
        }
    }

    @Transactional
    public GroupPlannerEntity saveGroupPlanner(Long uSeq, GroupPlannerCreateDTO groupPlanner, LocalDate parsedDate) {
        try {
            UserEntity userEntity = userRepository.findById(uSeq)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            StudyGroupEntity studyGroupEntity = studyGroupRepository.findById(groupPlanner.getSgSeq())
                    .orElseThrow(() -> new IllegalArgumentException("Study Group not found"));

            Optional<GroupPlannerEntity> groupPlannerEntityOptional = groupPlannerRepository.findByUser_uSeqAndStudyGroup_sgSeqAndIsDeletedAndRegistered(uSeq, groupPlanner.getSgSeq(), false, parsedDate);

            // 해당 날짜에 이미 만들어둔 플래너가 있는 경우
            if(groupPlannerEntityOptional.isPresent()){
                return null;
            }

            GroupPlannerEntity groupPlannerEntity = new GroupPlannerEntity();
            groupPlannerEntity.setStudyGroup(studyGroupEntity);
            groupPlannerEntity.setUser(userEntity);
            groupPlannerEntity.setRegistered(parsedDate);
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
            return groupPlannerEntity;
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return null;
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

    public Map<LocalDate, Long> getGroupGrass(GroupPlannerRequestDTO groupPlannerRequestDTO, YearMonth yearMonth) {
        try{
            StudyGroupEntity studyGroupEntity = studyGroupRepository.findById(groupPlannerRequestDTO.getSgSeq())
                    .orElseThrow(() -> new IllegalArgumentException("Study Group not found"));

            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            List<GroupPlannerEntity> groupPlannerEntities = groupPlannerRepository.findByUser_uSeqAndStudyGroup_sgSeqAndIsDeletedAndRegisteredBetween(groupPlannerRequestDTO.getUSeq(), groupPlannerRequestDTO.getSgSeq(), false, startDate, endDate);

            Map<LocalDate, Long> completedTaskCounts = new HashMap<>();
            for(GroupPlannerEntity groupPlanner: groupPlannerEntities){
                long completedCount = groupPlanner.getGroupTodoEntityList().stream().filter(GroupTodoEntity::isCompleted).count();
                completedTaskCounts.put(groupPlanner.getRegistered(), completedCount);
            }
            return completedTaskCounts;
        }catch (Exception e){
            System.out.println("e:: "+e.getMessage());
            return null;
        }
    }
}
