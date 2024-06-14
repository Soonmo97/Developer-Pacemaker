package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.developer_pacemaker.entity.StudyGroupGrassEntity;

@Repository
public interface StudyGroupGrassRepository extends JpaRepository<StudyGroupGrassEntity, Long> {
}
