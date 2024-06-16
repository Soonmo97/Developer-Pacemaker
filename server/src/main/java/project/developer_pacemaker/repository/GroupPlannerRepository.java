package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.developer_pacemaker.dto.groupPlanner.GroupPlannerDTO;
import project.developer_pacemaker.entity.GroupPlannerEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface GroupPlannerRepository extends JpaRepository<GroupPlannerEntity, Long> {
    Optional<GroupPlannerEntity> findByUser_uSeqAndGpSeqAndIsDeletedAndRegistered(long sgSeq, long uSeq, boolean b, LocalDate parsedDate);
}
