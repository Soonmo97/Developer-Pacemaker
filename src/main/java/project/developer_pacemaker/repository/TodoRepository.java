package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.developer_pacemaker.entity.PlannerEntity;
import project.developer_pacemaker.entity.TodoEntity;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findByPlanner(PlannerEntity plannerEntity);

    Optional<TodoEntity> findBytSeqAndIsDeletedFalse(long tSeq);
}
