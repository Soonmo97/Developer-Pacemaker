package project.developer_pacemaker.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.developer_pacemaker.entity.GroupMembersEntity;
import project.developer_pacemaker.entity.UserEntity;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMembersEntity, Long> {

    @Query("SELECT gm FROM GroupMembersEntity gm JOIN FETCH gm.user WHERE gm.studyGroup.sgSeq = :sgSeq")
    List<GroupMembersEntity> findGroupMemberByStudyGroup(long sgSeq);

    @Query("SELECT gm FROM GroupMembersEntity gm WHERE gm.user.uSeq = :uSeq AND gm.studyGroup.sgSeq = :sgSeq")
    GroupMembersEntity findByUSeqAndStudyGroup(long uSeq, long sgSeq);

    @Query("SELECT gm FROM GroupMembersEntity gm WHERE gm.user.uSeq = :uSeq")
    GroupMembersEntity findByUSeq(long uSeq);

    @Query("UPDATE GroupMembersEntity gm SET gm.isDeleted = true WHERE gm.user.uSeq = :uSeq")
    @Modifying
    @Transactional
    void deletedByUSeq(long uSeq);
}
