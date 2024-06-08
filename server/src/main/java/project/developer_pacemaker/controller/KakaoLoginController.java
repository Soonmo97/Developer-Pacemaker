package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.developer_pacemaker.dto.UserDTO;
import project.developer_pacemaker.service.KakaoService;

@RestController
@RequestMapping("/api/kakao")
public class KakaoLoginController {

    @Autowired
    private KakaoService kakaoService;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 API 입니다.")
    @GetMapping("/kakaoLogin")
    public UserDTO login(@RequestParam String code) {
        return kakaoService.kakaoLogin(code);
    }
}
