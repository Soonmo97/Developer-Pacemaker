package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.developer_pacemaker.entity.RecruitmentBoardEntity;
import java.util.List;

@Repository
public interface RecruitmentBoardRepository extends JpaRepository<RecruitmentBoardEntity, Long> {
    @Query("SELECT r FROM RecruitmentBoardEntity r WHERE r.name LIKE %:name% AND r.isDeleted = false")
    List<RecruitmentBoardEntity> findByName(@Param("name") String name);

    @Query("SELECT rb.studyGroup.sgSeq FROM RecruitmentBoardEntity rb")
    List<Long> findAllSgSeqInRecruitmentBoard();
}
