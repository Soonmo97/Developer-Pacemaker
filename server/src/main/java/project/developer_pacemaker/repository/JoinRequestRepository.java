package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.developer_pacemaker.entity.JoinRequestEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;

import java.util.List;

public interface JoinRequestRepository  extends JpaRepository<JoinRequestEntity, Long> {
    List<JoinRequestEntity> findByStudyGroup(StudyGroupEntity studyGroupEntity);
}
