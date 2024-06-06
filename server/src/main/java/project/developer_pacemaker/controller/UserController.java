package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.ResErrorDTO;
import project.developer_pacemaker.dto.UserDTO;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.security.TokenProvider;
import project.developer_pacemaker.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    final private UserService userService;
    final private BCryptPasswordEncoder passwordEncoder;
    final private TokenProvider tokenProvider;
    @Autowired
    public UserController(final UserService userService, final BCryptPasswordEncoder passwordEncoder, final TokenProvider tokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }


    @Operation(summary = "회원가입", description = "회원가입 API 입니다.")
    @PostMapping()
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // 비밀번호 유효성 검사
            if (userDTO.getPw() == null || userDTO.getPw().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                    .error("Password cannot be empty")
                    .build());
            }

            UserEntity user = UserEntity.builder()
                .nickname(userDTO.getNickname())
                .email(userDTO.getEmail())
                .pw(passwordEncoder.encode(userDTO.getPw()))
                .build();

            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = userDTO.builder()
                .email(registeredUser.getEmail())
                .nickname(registeredUser.getNickname())
                .social(registeredUser.getSocial())
                .build();

            return ResponseEntity.ok(responseUserDTO);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "닉네임 중복체크", description = "닉네임 중복체크 API 입니다.")
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        try {
            boolean isDuplicate = userService.isDuplicateNickname(nickname);
            return ResponseEntity.ok(isDuplicate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "이메일 중복체크", description = "이메일 중복체크(회원가입, 비밀번호 찾기) API 입니다.")
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        try {
            boolean isDuplicate = userService.isDuplicateEmail(email);
            return ResponseEntity.ok(isDuplicate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "로그인", description = "로그인 API 입니다.")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        try {
            UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPw());

            if (user != null) {
                // [token 인증 방식] client 에게 jwt token 을 발급해 응답으로 전송
                String token = tokenProvider.create(user);
                final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .uSeq(user.getUSeq())
                    // token 을 세팅해서 전달할 예정
                    .token(token)
                    .build();

                return ResponseEntity.ok().body(responseUserDTO);
            } else {
                return ResponseEntity
                    .badRequest()
                    .body("login failed");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

    @Operation(summary = "비밀번호 찾기(재설정)", description = "비밀번호 찾기(재설정) API 입니다.")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody UserDTO userDTO
    ) {
        try {
            UserEntity existingUser = userService.findByEmail(userDTO.getEmail());
            if (existingUser != null) {
                existingUser.setPw(passwordEncoder.encode(userDTO.getPw()));
                userService.saveAs(existingUser); // 사용자 정보 저장

                return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("입력하신 이메일은 존재하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "마이페이지 프로필 조회", description = "마이페이지 프로필 조회 API 입니다.")
    @GetMapping()
    public ResponseEntity<?> getMyPageProfile(@AuthenticationPrincipal String uSeq) {
        try {
            UserEntity user = userService.findByUSeq(uSeq);
            UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .img(user.getImg())
                .nickname(user.getNickname())
                .social(user.getSocial())
                .build();

            return ResponseEntity.ok(userDTO);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResErrorDTO.builder()
                .error(e.getMessage())
                .build()
            );
        }
    }

}
