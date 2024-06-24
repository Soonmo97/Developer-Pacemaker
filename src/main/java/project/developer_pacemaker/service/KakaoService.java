package project.developer_pacemaker.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        log.warn("kakaoLogin {}", code);
        String accessToken = getAccessToken(code);
        return processKakaoLogin(accessToken);
    }

//    // 카카오에서 발급 받은 액세스 토큰으로 로그인
//    public UserDTO kakaoLoginWithAccessToken(String accessToken) {
//        return processKakaoLogin(accessToken);
//    }

    // 공통된 로그인 처리 로직을 메서드로 분리
    private UserDTO processKakaoLogin(String accessToken) {
        UserDTO kakaoUser = getKakaoUserInfo(accessToken);

        UserEntity userEntity;
        if (!userRepository.existsByNickname(kakaoUser.getNickname())) {
            userEntity = UserEntity.builder()
                    .email(kakaoUser.getEmail())
                    .nickname(kakaoUser.getNickname())
                    .pw(null)
                    .img(null) // 이미지 경로로 수정해야함
                    .social("true")
                    .build();
            userRepository.save(userEntity);
        } else {
            userEntity = userRepository.findByNickname(kakaoUser.getNickname());
        }

        log.warn("userEntity {}", userEntity.getNickname());

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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();

        String response;
        try {
            response = restTemplate.postForObject(KAKAO_TOKEN_URL, request, String.class);
            log.info("Access Token Response: {}", response);
        } catch (Exception e) {
            log.error("error kakao login 카카오로부터 엑세스 토큰을 발급하는데 실패했습니다.", e);
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
        log.warn("getKakaoUserInfo 1 {}", accessToken);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");
        log.warn("getKakaoUserInfo 2 {}", jsonObject.getJSONObject("kakao_account"));
        log.warn("getKakaoUserInfo 3 {}", kakaoAccount.getString("email"));
        return UserDTO.builder()
                .email(kakaoAccount.getString("email"))
                .nickname(kakaoAccount.getJSONObject("profile").getString("nickname"))
                .pw(null)
                .img(null)
                .social("true")
                .build();
    }
}
