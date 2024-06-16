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

    public boolean saveGroupTodo(Long uSeq, long gpSeq, GroupTodoCreateDTO groupTodo) {
        Optional<GroupPlannerEntity> groupPlannerEntityOptional = groupPlannerRepository.findById(gpSeq);

        if(groupPlannerEntityOptional.isPresent()){
            GroupPlannerEntity groupPlannerEntity = groupPlannerEntityOptional.get();
            if(groupPlannerEntity.getUser().getUSeq()!=uSeq){
                return false;
            }
            GroupTodoEntity groupTodoEntity = new GroupTodoEntity();
            groupTodoEntity.setGroupPlanner(groupPlannerEntity);
            groupTodoEntity.setContent(groupTodo.getContent());
            groupTodoEntity.setCompleted(groupTodo.getIsCompleted()!=null ? groupTodo.getIsCompleted():false);
            groupTodoRepository.save(groupTodoEntity);
            return true;
        }
        return false;
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
}
