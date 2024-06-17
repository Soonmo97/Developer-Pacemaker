package project.developer_pacemaker.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.developer_pacemaker.dto.planner.PlannerCreateDTO;
import project.developer_pacemaker.dto.planner.PlannerDTO;
import project.developer_pacemaker.dto.planner.TodoCreateDTO;
import project.developer_pacemaker.dto.planner.TodoDTO;
import project.developer_pacemaker.entity.ArchivedTodoEntity;
import project.developer_pacemaker.entity.PlannerEntity;
import project.developer_pacemaker.entity.TodoEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.ArchivedTodoRepository;
import project.developer_pacemaker.repository.PlannerRepository;
import project.developer_pacemaker.repository.TodoRepository;
import project.developer_pacemaker.repository.UserRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlannerService {

    final private PlannerRepository plannerRepository;
    final private TodoRepository todoRepository;
    final private ArchivedTodoRepository archivedTodoRepository;
    final private UserRepository userRepository;

    public PlannerService(PlannerRepository plannerRepository, TodoRepository todoRepository, UserRepository userRepository, ArchivedTodoRepository archivedTodoRepository) {
        this.plannerRepository = plannerRepository;
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.archivedTodoRepository = archivedTodoRepository;
    }



    public List<PlannerDTO> getPlannerByuSeq(Long uSeq) {
        Sort sort = sortBypSeq();
        List<PlannerEntity> plannerEntities = plannerRepository.findByUser_uSeqAndIsDeleted(uSeq, false, sort);
        return plannerEntities.stream()
                .map(planner -> new PlannerDTO(
                        planner.getPSeq(),
                        planner.getRegistered(),
                        planner.getTodoEntities().stream()
                                .map(todo -> new TodoDTO(todo.getTSeq(),todo.getContent(), todo.getDuration(), todo.isCompleted()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    private Sort sortBypSeq() {
        return Sort.by(Sort.Direction.DESC, "pSeq");
    }

    @Transactional
    public boolean savePlanner(Long uSeq, PlannerCreateDTO planner, String date) {

        UserEntity userEntity = userRepository.findById(uSeq)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String cleanedDate = date.trim().replaceAll("[^\\d-]", "");
        LocalDate parsedDate = LocalDate.parse(cleanedDate);

        Optional<PlannerEntity> plannerEntityOptional = plannerRepository.findByUser_uSeqAndIsDeletedAndRegistered(uSeq, false, parsedDate);

        // 해당 날짜에 이미 만들어둔 플래너가 있는 경우
        if(plannerEntityOptional.isPresent()){
            return false;
        }

        PlannerEntity plannerEntity = new PlannerEntity();
        plannerEntity.setUser(userEntity);
        plannerEntity.setRegistered(parsedDate);
        plannerRepository.save(plannerEntity);

        List<TodoCreateDTO> todoCreateDTOList = planner.getTodoCreateDTOList();

        if(todoCreateDTOList != null && !todoCreateDTOList.isEmpty()){
            for(TodoCreateDTO todoDTO : todoCreateDTOList){
                TodoEntity todoEntity = new TodoEntity();
                todoEntity.setPlanner(plannerEntity);
                todoEntity.setContent(todoDTO.getContent());
                todoEntity.setDuration(todoDTO.getDuration() != null ? todoDTO.getDuration() : 0.0f);
                todoEntity.setCompleted(todoDTO.getIsCompleted() != null ? todoDTO.getIsCompleted() : false);
                todoRepository.save(todoEntity);
            }
        }
        return true;
    }

    public boolean deletePlannerBypSeq(Long currentUSeq, long pSeq) {
        try{
            PlannerEntity plannerEntity = plannerRepository.findById(pSeq)
                    .orElseThrow(() -> new RuntimeException("Planner data with pSeq " + pSeq + " not found"));

            if(plannerEntity.getUser().getUSeq()!=currentUSeq){
                return false;
            }
            plannerEntity.setDeleted(true);
            plannerRepository.save(plannerEntity);

            List<TodoEntity> todoEntities = todoRepository.findByPlanner(plannerEntity);

            if(todoEntities != null && !todoEntities.isEmpty()){
                for(TodoEntity todoEntity:todoEntities) {
                    ArchivedTodoEntity archivedTodoEntity = new ArchivedTodoEntity();
                    archivedTodoEntity.setPSeq(todoEntity.getPlanner().getPSeq());
                    archivedTodoEntity.setContent(todoEntity.getContent());
                    archivedTodoEntity.setDuration(todoEntity.getDuration());
                    archivedTodoEntity.setCompleted(todoEntity.isCompleted());
                    archivedTodoRepository.save(archivedTodoEntity);

                    todoRepository.delete(todoEntity);
                }
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<TodoDTO> getPlannerByDate(Long uSeq, String date) {
        try {
            UserEntity userEntity = userRepository.findById(uSeq)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            String cleanedDate = date.trim().replaceAll("[^\\d-]", "");
            LocalDate parsedDate = LocalDate.parse(cleanedDate);

            Optional<PlannerEntity> plannerEntityOptional = plannerRepository.findByUser_uSeqAndIsDeletedAndRegistered(uSeq, false, parsedDate);
            if(plannerEntityOptional.isPresent()){
                PlannerEntity plannerEntity = plannerEntityOptional.get();
                if(plannerEntity.getUser().getUSeq()!=uSeq){
                    return null;
                }
                List<TodoEntity> todoEntities = todoRepository.findByPlanner(plannerEntity);
                return todoEntities.stream()
                        .map(todo -> new TodoDTO(todo.getTSeq(),todo.getContent(), todo.getDuration(), todo.isCompleted()))
                        .collect(Collectors.toList());
            }
            return null;
        }catch (Exception e){
            System.out.println("e::"+e.getMessage());
            return null;
        }
    }

    public Map<LocalDate, Long> getUserGrass(Long uSeq, YearMonth yearMonth) {
        try{
            UserEntity userEntity = userRepository.findById(uSeq)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            List<PlannerEntity> plannerEntities = plannerRepository.findByUser_uSeqAndIsDeletedAndRegisteredBetween(uSeq, false, startDate, endDate);

            Map<LocalDate, Long> completedTaskCounts = new HashMap<>();
            for(PlannerEntity planner:plannerEntities){
                long completedCount = planner.getTodoEntities().stream().filter(TodoEntity::isCompleted).count();
                completedTaskCounts.put(planner.getRegistered(), completedCount);
            }
            return completedTaskCounts;
        }catch (Exception e){
            System.out.println("e:: "+e.getMessage());
            return null;
        }
    }
}
