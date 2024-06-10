package project.developer_pacemaker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.StudyGroupDTO;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.StudyGroupRepository;
import project.developer_pacemaker.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class StudyGroupService {

    final private StudyGroupRepository studyGroupRepository;
    final private UserRepository userRepository;

    @Autowired
    public StudyGroupService(StudyGroupRepository studyGroupRepository, UserRepository userRepository) {
        this.studyGroupRepository = studyGroupRepository;
        this.userRepository = userRepository;
    }

    public StudyGroupEntity create(final StudyGroupEntity studyGroupEntity) {
        if (studyGroupEntity == null || studyGroupEntity.getName() == null ||
            studyGroupEntity.getName().trim().isEmpty()
        ) {
            throw new RuntimeException("Invalid arguments");
        }

        final String name = studyGroupEntity.getName();

        if (studyGroupRepository.existsByName(name)) {
            log.warn("studyGroup name already exists {}", name);
            throw new RuntimeException("studyGroup name already exists");
        }

        return studyGroupRepository.save(studyGroupEntity);
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

    public Boolean isDuplicateName(final String name) {
        return studyGroupRepository.existsByName(name);
    }

    public String delete(final String uSeq, final long sgSeq) {
        StudyGroupEntity studyGroup = studyGroupRepository.findById(sgSeq).orElseThrow(()->new RuntimeException("RuntimeException"));
        if (Long.parseLong(uSeq) != studyGroup.getUser().getUSeq()) {
            log.warn("스터디그룹의 그룹장이 아닙니다. {}", uSeq);
            throw new RuntimeException("스터디그룹의 그룹장이 아닙니다.");
        }
        studyGroupRepository.deleteById(sgSeq);
        return "스터디그룹이 삭제되었습니다.";
    }

    public List<StudyGroupEntity> getAll() {
        return studyGroupRepository.findAll();
    }

    public List<StudyGroupEntity> myGetAll(String uSeq) {
        UserEntity user = findByUSeq(uSeq);
        return studyGroupRepository.findByUser(user);
    }
}
