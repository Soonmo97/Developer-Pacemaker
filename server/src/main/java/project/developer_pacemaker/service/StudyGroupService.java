package project.developer_pacemaker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.StudyGroupDTO;
import project.developer_pacemaker.entity.GroupMembersEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.GroupMemberRepository;
import project.developer_pacemaker.repository.StudyGroupRepository;
import project.developer_pacemaker.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class StudyGroupService {

    final private StudyGroupRepository studyGroupRepository;
    final private UserRepository userRepository;

    final private GroupMemberRepository groupMemberRepository;

    @Autowired
    public StudyGroupService(final StudyGroupRepository studyGroupRepository, final UserRepository userRepository
    , final GroupMemberRepository groupMemberRepository) {
        this.studyGroupRepository = studyGroupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository =groupMemberRepository;
    }

    public StudyGroupEntity create(final StudyGroupEntity studyGroupEntity) {
        if (studyGroupEntity == null || studyGroupEntity.getName() == null ||
            studyGroupEntity.getName().trim().isEmpty() || studyGroupEntity.getGoal() == null ||
            studyGroupEntity.getGoal().trim().isEmpty()
        ) {
            throw new RuntimeException("Invalid arguments");
        }

        final String name = studyGroupEntity.getName();

        if (studyGroupRepository.existsByName(name)) {
            log.warn("studyGroup name already exists {}", name);
            throw new RuntimeException("studyGroup name already exists");
        }
        studyGroupEntity.setCurrent(studyGroupEntity.getCurrent() + 1);
        StudyGroupEntity resStudyGroup = studyGroupRepository.save(studyGroupEntity);
        // 스터디그룹 생성 시 그룹장 그룹멤버스에 추가
        GroupMembersEntity groupMembers = GroupMembersEntity.builder()
            .user(studyGroupEntity.getUser())
            .studyGroup(studyGroupEntity)
            .build();
        groupMemberRepository.save(groupMembers);
        return resStudyGroup;
    }

    public UserEntity findByUSeq(final String uSeq) {
        Long uSeqLong = Long.parseLong(uSeq);
        return userRepository.findById(uSeqLong).orElseThrow(()->new RuntimeException("RuntimeException"));
    }

    public StudyGroupEntity updateName(final String uSeq, final StudyGroupDTO studyGroupDTO) {
        StudyGroupEntity studyGroup= studyGroupRepository.findById(studyGroupDTO.getSgSeq()).orElseThrow(()->new RuntimeException("RuntimeException"));
        if (studyGroup != null) {
            if (Long.parseLong(uSeq) != studyGroup.getUser().getUSeq()) {
                log.warn("스터디그룹의 그룹장이 아닙니다. {}", uSeq);
                throw new RuntimeException("스터디그룹의 그룹장이 아닙니다.");
            }
            studyGroup.setName(studyGroupDTO.getName());
            return studyGroupRepository.save(studyGroup);
        }
        return null;
    }

    public StudyGroupEntity updateGoal(final String uSeq, final StudyGroupDTO studyGroupDTO) {
        StudyGroupEntity studyGroup= studyGroupRepository.findById(studyGroupDTO.getSgSeq()).orElseThrow(()->new RuntimeException("RuntimeException"));
        if (studyGroup != null) {
            if (Long.parseLong(uSeq) != studyGroup.getUser().getUSeq()) {
                log.warn("스터디그룹의 그룹장이 아닙니다. {}", uSeq);
                throw new RuntimeException("스터디그룹의 그룹장이 아닙니다.");
            }
            studyGroup.setGoal(studyGroupDTO.getGoal());
            return studyGroupRepository.save(studyGroup);
        }
        return null;
    }

    public StudyGroupEntity updateStatus(final String uSeq, final StudyGroupDTO studyGroupDTO) {
        StudyGroupEntity studyGroup= studyGroupRepository.findById(studyGroupDTO.getSgSeq()).orElseThrow(()->new RuntimeException("RuntimeException"));
        if (studyGroup != null) {
            if (Long.parseLong(uSeq) != studyGroup.getUser().getUSeq()) {
                log.warn("스터디그룹의 그룹장이 아닙니다. {}", uSeq);
                throw new RuntimeException("스터디그룹의 그룹장이 아닙니다.");
            }
            studyGroup.setStatus(!studyGroup.isStatus());
            return studyGroupRepository.save(studyGroup);
        }
        return null;
    }


    public Boolean isDuplicateName(final String name) {
        return studyGroupRepository.existsByName(name);
    }

    public StudyGroupEntity delete(final String uSeq, final long sgSeq) {
        StudyGroupEntity studyGroup = studyGroupRepository.findById(sgSeq).orElseThrow(()->new RuntimeException("RuntimeException"));
        if (Long.parseLong(uSeq) != studyGroup.getUser().getUSeq()) {
            log.warn("스터디그룹의 그룹장이 아닙니다. {}", uSeq);
            throw new RuntimeException("스터디그룹의 그룹장이 아닙니다.");
        }
        studyGroupRepository.deleteById(sgSeq);
        return studyGroup;
    }

    public List<StudyGroupEntity> getAll() {
        return studyGroupRepository.findAll();
    }

    public List<StudyGroupEntity> getOpenAll() {
            List<StudyGroupEntity> studyGroups = studyGroupRepository.findAll();
            for (StudyGroupEntity studyGroup : studyGroups) {
                if(studyGroup.isStatus() == false) {
                    studyGroups.remove(studyGroup);
                }
            }
            return studyGroups;
    }

    public List<StudyGroupEntity> myGetAll(final String uSeq) {
        UserEntity user = findByUSeq(uSeq);
        return studyGroupRepository.findByUser(user);
    }

    public boolean getStatus(final long sgSeq) {
        StudyGroupEntity studyGroup = studyGroupRepository.findById(sgSeq).orElseThrow(()-> new RuntimeException("RuntimeException"));
        return studyGroup.isStatus();
    }

}
