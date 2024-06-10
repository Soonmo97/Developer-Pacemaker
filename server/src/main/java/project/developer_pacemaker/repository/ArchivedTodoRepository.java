package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.developer_pacemaker.entity.ArchivedTodoEntity;

public interface ArchivedTodoRepository extends JpaRepository<ArchivedTodoEntity, Long> {
}
