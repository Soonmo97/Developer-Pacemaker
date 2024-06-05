package project.developer_pacemaker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.UserRepository;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if (userEntity == null || userEntity.getEmail() == null || userEntity.getNickname() == null ||
            userEntity.getEmail().trim().isEmpty() || userEntity.getNickname().trim().isEmpty()
        ) {
            throw new RuntimeException("Invalid arguments");
        }

        final String email = userEntity.getEmail();
        final String nickname = userEntity.getNickname();

        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByNickname(nickname)) {
            log.warn("Nickname already exists {}", email);
            throw new RuntimeException("Nickname already exists");
        }

        // UserEntity 를 DB에 저장.
        // save를 했을 때 반환되는 객체는 Entity 객체
        return userRepository.save(userEntity);
    }

    public Boolean isDuplicateEmail(final String email) {
        return userRepository.existsByEmail(email);
    }
    public Boolean isDuplicateNickname(final String nickname) {
        return userRepository.existsByNickname(nickname);
    }

}
