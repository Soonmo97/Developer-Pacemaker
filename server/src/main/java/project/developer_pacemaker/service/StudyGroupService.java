package project.developer_pacemaker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.StudyGroupRepository;
import project.developer_pacemaker.repository.UserRepository;

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
            studyGroupEntity.getName().trim().isEmpty() || studyGroupEntity.getMax() == 0
        ) {
            throw new RuntimeException("Invalid arguments");
        }

        final String name = studyGroupEntity.getName();
        final int max = studyGroupEntity.getMax();

        if (studyGroupRepository.existsByName(name)) {
            log.warn("studyGroup name already exists {}", name);
            throw new RuntimeException("studyGroup name already exists");
        }

        if (max > 15) {
            log.warn("studyGroup max over {}", max);
            throw new RuntimeException("studyGroup 최대 정원은 15명 입니다.");
        }

        if (max < 0)
        {
            log.warn("studyGroup max is cannot negative");
            throw new RuntimeException("studyGroup 최대 정원은 15명 입니다.");
        }

        return studyGroupRepository.save(studyGroupEntity);
    }

    public UserEntity findByUSeq(final String uSeq) {
        Long uSeqLong = Long.parseLong(uSeq);
        return userRepository.findById(uSeqLong).orElseThrow(()->new RuntimeException("RuntimeException"));
    }
}
