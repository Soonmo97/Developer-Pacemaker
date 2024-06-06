package project.developer_pacemaker.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.developer_pacemaker.config.social.KakaoProperties;
import project.developer_pacemaker.dto.UserDTO;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.UserRepository;
import project.developer_pacemaker.security.TokenProvider;

@Service
@Slf4j
public class KakaoService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final KakaoProperties kakaoProperties;

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Autowired
    public KakaoService(UserRepository userRepository, TokenProvider tokenProvider, KakaoProperties kakaoProperties) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.kakaoProperties = kakaoProperties;
    }

    public UserDTO kakaoLogin(String code) {
        String accessToken = getAccessToken(code);
        return processKakaoLogin(accessToken);
    }

    // 카카오에서 발급 받은 액세스 토큰으로 로그인
    public UserDTO kakaoLoginWithAccessToken(String accessToken) {
        return processKakaoLogin(accessToken);
    }

    // 공통된 로그인 처리 로직을 메서드로 분리
    private UserDTO processKakaoLogin(String accessToken) {
        UserDTO kakaoUser = getKakaoUserInfo(accessToken);

        UserEntity userEntity;
        if (!userRepository.existsByNickname(kakaoUser.getNickname())) {
            userEntity = UserEntity.builder()
                    .email("kakaoLoginUser")
                    .nickname(kakaoUser.getNickname())
                    .pw(null)
                    .img(null) // 이미지 경로로 수정해야함
                    .social("true")
                    .build();
            userRepository.save(userEntity);
        } else {
            userEntity = userRepository.findByNickname(kakaoUser.getNickname());
        }

        // JWT 토큰 생성
        String token = tokenProvider.create(userEntity);
        return UserDTO.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .uSeq(userEntity.getUSeq())
                .token(token)
                .build();
    }

    // 엑세스 토큰 가져오기
    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = KAKAO_TOKEN_URL + "?grant_type=authorization_code&client_id=" + kakaoProperties.getClientId() + "&redirect_uri=" + kakaoProperties.getRedirectUri() + "&code=" + code;

        String response;
        try {
            response = restTemplate.postForObject(requestUrl, null, String.class);
            log.info("Access Token Response: {}", response);
        } catch (Exception e) {
            log.error("카카오로부터 엑세스 토큰을 발급하는데 실패했습니다.", e);
            throw new RuntimeException("카카오로부터 엑세스 토큰을 발급하는데 실패했습니다.");
        }
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("access_token");
    }

    // 카카오 사용자 정보 가져오기
    private UserDTO getKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = KAKAO_USER_INFO_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");

        return UserDTO.builder()
                .email("kakao") // 실제 이메일 정보를 반영할 수 있습니다.
                .nickname(kakaoAccount.getJSONObject("profile").getString("nickname"))
                .pw(null)
                .img(null)
                .social("true")
                .build();
    }
}
