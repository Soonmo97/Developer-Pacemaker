package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.developer_pacemaker.entity.StudyGroupEntity;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroupEntity, Long> {
    Boolean existsByName(String name);
}
