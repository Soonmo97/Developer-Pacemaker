package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.ResErrorDTO;
import project.developer_pacemaker.dto.UserDTO;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입", description = "회원가입 API 입니다.")
    @PostMapping()
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
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

}
