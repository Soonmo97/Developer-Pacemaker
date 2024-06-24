package project.developer_pacemaker.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.developer_pacemaker.entity.ReportEntity;

import java.util.List;

@Repository
public interface ReportRepository  extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByUser_uSeqAndIsDeleted(Long uSeq, boolean isDeleted, Sort sort);
}
