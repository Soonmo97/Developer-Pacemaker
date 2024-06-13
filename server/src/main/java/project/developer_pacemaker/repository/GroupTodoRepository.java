package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.developer_pacemaker.entity.GroupTodoEntity;

public interface GroupTodoRepository extends JpaRepository<GroupTodoEntity, Long> {
}
