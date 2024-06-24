package project.developer_pacemaker.service;

import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.planner.TodoCreateDTO;
import project.developer_pacemaker.dto.planner.TodoDTO;
import project.developer_pacemaker.entity.ArchivedTodoEntity;
import project.developer_pacemaker.entity.PlannerEntity;
import project.developer_pacemaker.entity.TodoEntity;
import project.developer_pacemaker.repository.ArchivedTodoRepository;
import project.developer_pacemaker.repository.PlannerRepository;
import project.developer_pacemaker.repository.TodoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {
    final private TodoRepository todoRepository;
    final private PlannerRepository plannerRepository;
    final private ArchivedTodoRepository archivedTodoRepository;

    public TodoService(TodoRepository todoRepository, PlannerRepository plannerRepository, ArchivedTodoRepository archivedTodoRepository) {
        this.todoRepository = todoRepository;
        this.plannerRepository = plannerRepository;
        this.archivedTodoRepository = archivedTodoRepository;
    }

    public List<TodoDTO> getTodoBypSeq(Long currentUSeq, long pSeq) {
        Optional<PlannerEntity> plannerEntityOptional = plannerRepository.findBypSeqAndIsDeletedFalse(pSeq);

        if(plannerEntityOptional.isPresent()){
            PlannerEntity plannerEntity = plannerEntityOptional.get();
            if(plannerEntity.getUser().getUSeq()!=currentUSeq){
                return null;
            }
            List<TodoEntity> todoEntities = todoRepository.findByPlanner(plannerEntity);
            return todoEntities.stream()
                    .map(todo -> new TodoDTO(todo.getTSeq(),todo.getContent(), todo.getDuration(), todo.isCompleted()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public TodoEntity saveTodo(Long currentUSeq, long pSeq, TodoCreateDTO todo) {
        Optional<PlannerEntity> plannerEntityOptional = plannerRepository.findBypSeqAndIsDeletedFalse(pSeq);

        if(plannerEntityOptional.isPresent()){
            PlannerEntity plannerEntity = plannerEntityOptional.get();
            if(plannerEntity.getUser().getUSeq()==currentUSeq){
                TodoEntity todoEntity = new TodoEntity();
                todoEntity.setPlanner(plannerEntity);
                todoEntity.setContent(todo.getContent());
                todoEntity.setDuration(todo.getDuration() != null ? todo.getDuration() : 0.0f);
                todoEntity.setCompleted(todo.getIsCompleted() != null ? todo.getIsCompleted() : false);
                todoRepository.save(todoEntity);
                return todoEntity;
            }else{
               return null;
            }
        }
        return null;
    }

    public boolean updateTodo(Long currentUSeq, long tSeq, TodoDTO todo) {
        try{
            Optional<TodoEntity> todoEntityOptional = todoRepository.findBytSeqAndIsDeletedFalse(tSeq);
            if(todoEntityOptional.isPresent()){
                TodoEntity todoEntity = todoEntityOptional.get();
                PlannerEntity plannerEntity = todoEntity.getPlanner();
                if(plannerEntity.getUser().getUSeq()==currentUSeq){
                    todoEntity.setContent(todo.getContent());
                    todoEntity.setDuration(todo.getDuration() != null ? todo.getDuration() : 0.0f);
                    todoEntity.setCompleted(todo.getIsCompleted() != null ? todo.getIsCompleted() : false);
                    todoRepository.save(todoEntity);
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    public boolean deleteTodo(Long currentUSeq, long tSeq) {
        try{
            Optional<TodoEntity> todoEntityOptional = todoRepository.findBytSeqAndIsDeletedFalse(tSeq);

            if(todoEntityOptional.isPresent()){
                TodoEntity todoEntity = todoEntityOptional.get();
                PlannerEntity plannerEntity = todoEntity.getPlanner();
                if(plannerEntity.getUser().getUSeq()==currentUSeq){
                    ArchivedTodoEntity archivedTodoEntity = new ArchivedTodoEntity();
                    archivedTodoEntity.setPSeq(todoEntity.getPlanner().getPSeq());
                    archivedTodoEntity.setContent(todoEntity.getContent());
                    archivedTodoEntity.setDuration(todoEntity.getDuration());
                    archivedTodoEntity.setCompleted(todoEntity.isCompleted());
                    archivedTodoRepository.save(archivedTodoEntity);

                    todoRepository.delete(todoEntity);
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    public boolean patchComplete(Long currentUSeq, long tSeq) {
        try{
            Optional<TodoEntity> todoEntityOptional = todoRepository.findBytSeqAndIsDeletedFalse(tSeq);
            if(todoEntityOptional.isPresent()){
                TodoEntity todoEntity = todoEntityOptional.get();
                PlannerEntity plannerEntity = todoEntity.getPlanner();
                if(plannerEntity.getUser().getUSeq()==currentUSeq){
                    boolean patch = todoEntity.isCompleted();
                    todoEntity.setCompleted(!patch);
                    todoRepository.save(todoEntity);
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return false;
        }
    }
}
