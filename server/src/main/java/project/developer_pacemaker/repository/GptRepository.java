package project.developer_pacemaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.developer_pacemaker.entity.GptEntity;
import project.developer_pacemaker.entity.UserEntity;

import java.util.List;

@Repository
public interface GptRepository extends JpaRepository<GptEntity, Long> {
    List<GptEntity> findByUser_uSeq(long uSeq);


    List<GptEntity> findByUser_uSeqAndIsDeleted(long uSeq, boolean isDeleted);
}
