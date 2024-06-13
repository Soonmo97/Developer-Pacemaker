package project.developer_pacemaker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.GroupMembersDTO;
import project.developer_pacemaker.entity.GroupMembersEntity;
import project.developer_pacemaker.entity.StudyGroupEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.GroupMemberRepository;
import project.developer_pacemaker.repository.StudyGroupRepository;
import project.developer_pacemaker.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GroupMembersService {

    final private GroupMemberRepository groupMemberRepository;

    final private StudyGroupRepository studyGroupRepository;

    final private UserRepository userRepository;

    @Autowired
    public GroupMembersService(final GroupMemberRepository groupMemberRepository,
                               final StudyGroupRepository studyGroupRepository,
                               final UserRepository userRepository
    ) {
        this.groupMemberRepository = groupMemberRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.userRepository = userRepository;
    }

    public List<GroupMembersDTO> getGroupMembersByStudyGroup(final String sgSeq) {
        List<GroupMembersEntity> groupMembersInfo = groupMemberRepository.findGroupMemberByStudyGroup(Long.parseLong(sgSeq));
        List<GroupMembersDTO> groupMembers = new ArrayList<>();
        for (GroupMembersEntity groupMember : groupMembersInfo) {
            GroupMembersDTO groupMemberDTO = GroupMembersDTO.builder()
                .uSeq(groupMember.getUser().getUSeq())
                .nickname(groupMember.getUser().getNickname())
                .img(groupMember.getUser().getImg())
                .score(groupMember.getScore())
                .build();

            groupMembers.add(groupMemberDTO);
        }

        return groupMembers;
    }

    public UserEntity kickMember(final String uSeq, final Long sgSeq, final Long memberUSeq) {
        StudyGroupEntity studyGroup = studyGroupRepository.findById(sgSeq).orElseThrow(() -> new RuntimeException("RuntimeException"));
        if (Long.parseLong(uSeq) != studyGroup.getUser().getUSeq()) {
            log.warn("스터디그룹의 그룹장이 아닙니다. {}", uSeq);
            throw new RuntimeException("스터디그룹의 그룹장이 아닙니다.");
        }
        GroupMembersEntity member = groupMemberRepository.findByUSeqAndStudyGroup(memberUSeq,sgSeq);
        groupMemberRepository.delete(member);

        // 스터디그룹의 현재 인원에서 -1 업데이트
        studyGroup.setCurrent(studyGroup.getCurrent()-1);
        studyGroupRepository.save(studyGroup);

        UserEntity user = userRepository.findById(memberUSeq).orElseThrow(() -> new RuntimeException("RuntimeException"));;
        return user;
    }

    public GroupMembersEntity createMember(final String uSeq, final long sgSeq, final long memberUSeq) {
        UserEntity user = userRepository.findById(memberUSeq).orElseThrow(() -> new RuntimeException("RuntimeException"));
        StudyGroupEntity studyGroup = studyGroupRepository.findById(sgSeq).orElseThrow(() -> new RuntimeException("RuntimeException"));
        // 그룹장일 때만 추가 가능
        if (Long.parseLong(uSeq) != studyGroup.getUser().getUSeq()) {
            log.warn("스터디그룹의 그룹장이 아닙니다. {}", uSeq);
            throw new RuntimeException("스터디그룹의 그룹장이 아닙니다.");
        }

        // 정원(15명)미만일 때만 추가
        int current = studyGroup.getCurrent();
        if (current + 1 > 15) {
            log.warn("스터디그룹 정원이 꽉 차있습니다. (정원 초과) 현재 인원{}", current);
            throw new RuntimeException("스터디그룹 정원이 꽉 차있습니다. (정원 초과)");
        }

        GroupMembersEntity createGroupMember = GroupMembersEntity.builder()
            .user(user)
            .studyGroup(studyGroup)
            .build();
        // 추가
        GroupMembersEntity resGroupMember = groupMemberRepository.save(createGroupMember);
        // 스터디그룹 현재 인원에 +1
        studyGroup.setCurrent(studyGroup.getCurrent() + 1);
        studyGroupRepository.save(studyGroup);

        return groupMemberRepository.save(resGroupMember);
    }

    public UserEntity deleteMember(final String uSeq, final Long sgSeq) {
        StudyGroupEntity studyGroup = studyGroupRepository.findById(sgSeq).orElseThrow(() -> new RuntimeException("RuntimeException"));
        GroupMembersEntity member = groupMemberRepository.findByUSeqAndStudyGroup(Long.parseLong(uSeq),sgSeq);
        groupMemberRepository.delete(member);

        // 스터디그룹의 현재 인원에서 -1 업데이트
        studyGroup.setCurrent(studyGroup.getCurrent() - 1);
        studyGroupRepository.save(studyGroup);

        UserEntity user = userRepository.findById(Long.parseLong(uSeq)).orElseThrow(() -> new RuntimeException("RuntimeException"));;
        return user;
    }
}
