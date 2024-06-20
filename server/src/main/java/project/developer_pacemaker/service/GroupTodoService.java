package project.developer_pacemaker.service;

import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.groupPlanner.GroupTodoCreateDTO;
import project.developer_pacemaker.entity.GroupPlannerEntity;
import project.developer_pacemaker.entity.GroupTodoEntity;
import project.developer_pacemaker.repository.GroupPlannerRepository;
import project.developer_pacemaker.repository.GroupTodoRepository;

import java.util.Optional;

@Service
public class GroupTodoService {

    final private GroupPlannerRepository groupPlannerRepository;
    final private GroupTodoRepository groupTodoRepository;

    public GroupTodoService(GroupPlannerRepository groupPlannerRepository, GroupTodoRepository groupTodoRepository) {
        this.groupPlannerRepository = groupPlannerRepository;
        this.groupTodoRepository = groupTodoRepository;
    }

    public GroupTodoEntity saveGroupTodo(Long uSeq, long gpSeq, GroupTodoCreateDTO groupTodo) {
        Optional<GroupPlannerEntity> groupPlannerEntityOptional = groupPlannerRepository.findById(gpSeq);

        try {
            if (groupPlannerEntityOptional.isPresent()) {
                GroupPlannerEntity groupPlannerEntity = groupPlannerEntityOptional.get();
                if (groupPlannerEntity.getUser().getUSeq() != uSeq) {
                    System.out.println("본인의 플래너가 아니면 투두 추가 불가능");
                    return null;
                }
                GroupTodoEntity groupTodoEntity = new GroupTodoEntity();
                groupTodoEntity.setGroupPlanner(groupPlannerEntity);
                groupTodoEntity.setContent(groupTodo.getContent());
                groupTodoEntity.setCompleted(groupTodo.getIsCompleted() != null ? groupTodo.getIsCompleted() : false);
                groupTodoRepository.save(groupTodoEntity);
                return groupTodoEntity;
            }
            return null;
        }catch (Exception e){
            System.out.println("eeeeeeeeee"+e.getMessage());
            return null;
        }
    }

    public boolean updateGroupTodo(Long uSeq, long gtSeq, GroupTodoCreateDTO groupTodo) {
        try {
            Optional<GroupTodoEntity> groupTodoEntityOptional = groupTodoRepository.findById(gtSeq);
            if(groupTodoEntityOptional.isPresent()){
                GroupTodoEntity groupTodoEntity = groupTodoEntityOptional.get();
                GroupPlannerEntity groupPlannerEntity = groupTodoEntity.getGroupPlanner();
                // 본인만 수정 가능
                if(groupPlannerEntity.getUser().getUSeq() !=uSeq){
                    return false;
                }
                groupTodoEntity.setContent(groupTodo.getContent());
                groupTodoEntity.setCompleted(groupTodo.getIsCompleted() != null ? groupTodo.getIsCompleted():false);
                groupTodoRepository.save(groupTodoEntity);
                return true;
            }
            return false;
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return false;
        }
    }

    public boolean deleteGroupTodo(Long uSeq, long gtSeq) {
        try {
            Optional<GroupTodoEntity> groupTodoEntityOptional = groupTodoRepository.findById(gtSeq);

            if(groupTodoEntityOptional.isPresent()){
                GroupTodoEntity groupTodoEntity = groupTodoEntityOptional.get();
                GroupPlannerEntity groupPlannerEntity = groupTodoEntity.getGroupPlanner();
                // 본인만 삭제 가능
                if(groupPlannerEntity.getUser().getUSeq() != uSeq){
                    return false;
                }
                groupTodoRepository.delete(groupTodoEntity);
                return true;
            }
            return false;
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return false;
        }
    }

    public boolean patchComplete(Long uSeq, long gtSeq) {
        try{
            Optional<GroupTodoEntity> groupTodoEntityOptional = groupTodoRepository.findById(gtSeq);
            if(groupTodoEntityOptional.isPresent()){
                GroupTodoEntity groupTodoEntity = groupTodoEntityOptional.get();
                GroupPlannerEntity groupPlannerEntity = groupTodoEntity.getGroupPlanner();
                // 본인만 수정 가능
                if(groupPlannerEntity.getUser().getUSeq() !=uSeq){
                    return false;
                }
                boolean patch = groupTodoEntity.isCompleted();
                groupTodoEntity.setCompleted(!patch);
                groupTodoRepository.save(groupTodoEntity);
                return groupTodoEntity.isCompleted();
            }
            return false;
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return false;
        }
    }
}
