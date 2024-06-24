package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.developer_pacemaker.entity.GroupPlannerEntity;
import project.developer_pacemaker.entity.GroupTodoEntity;

import java.util.List;

public interface GroupTodoRepository extends JpaRepository<GroupTodoEntity, Long> {
    List<GroupTodoEntity> findByGroupPlanner(GroupPlannerEntity groupPlannerEntity);
}
