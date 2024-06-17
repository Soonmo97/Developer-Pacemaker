package project.developer_pacemaker.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import project.developer_pacemaker.entity.GroupPlannerEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GroupPlannerRepository extends JpaRepository<GroupPlannerEntity, Long> {
    Optional<GroupPlannerEntity> findByUser_uSeqAndStudyGroup_sgSeqAndIsDeletedAndRegistered(Long uSeq, long sgSeq, boolean b, LocalDate parsedDate);

    List<GroupPlannerEntity> findByUser_uSeqAndStudyGroup_sgSeqAndIsDeletedAndRegisteredBetween(long uSeq, long sgSeq, boolean b, LocalDate startDate, LocalDate endDate);
}
