package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;

import java.util.List;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroupEntity, Long> {
    Boolean existsByName(String name);

    List<StudyGroupEntity> findByUser(UserEntity user);

}
