package project.developer_pacemaker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.UserRepository;

@Service
@Slf4j
public class UserService {

    final private UserRepository userRepository;
    final private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

        return userRepository.save(userEntity);
    }

    public Boolean isDuplicateEmail(final String email) {
        return userRepository.existsByEmail(email);
    }
    public Boolean isDuplicateNickname(final String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public UserEntity getByCredentials(final String email, final String password) {

        UserEntity user = userRepository.findByEmail(email);

        if(user != null && passwordEncoder.matches(password, user.getPw())){
            return user;
        } else return null;
    }

    public UserEntity findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity saveAs(final UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity findByUSeq(final String uSeq) {
        Long uSeqLong = Long.parseLong(uSeq);
        return userRepository.findById(uSeqLong).orElseThrow(()->new RuntimeException("RuntimeException"));
    }

    public String deleteUser(final String uSeq) {
        Long uSeqLong = Long.parseLong(uSeq);
        userRepository.deleteById(uSeqLong);
        return "회원탈퇴 되었습니다.";
    }

    public UserEntity updateNickname(final String uSeq, String newNickname) {
        Long uSeqLong = Long.parseLong(uSeq);
        UserEntity user = userRepository.findById(uSeqLong).orElseThrow(()->new RuntimeException("RuntimeException"));
        if (user != null) {
            user.setNickname(newNickname);
            return userRepository.save(user);
        }
        return null;
    }

    public String updatePw(final String uSeq, String newPw) {
        Long uSeqLong = Long.parseLong(uSeq);
        UserEntity user = userRepository.findById(uSeqLong).orElseThrow(()->new RuntimeException("RuntimeException"));
        if (user != null) {
            user.setPw(passwordEncoder.encode(newPw));
            userRepository.save(user);
            return "비밀번호 변경완료";
        }
        return "존재하지 않는 사용자입니다.";
    }
}
