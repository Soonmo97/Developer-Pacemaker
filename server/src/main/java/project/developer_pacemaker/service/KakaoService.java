package project.developer_pacemaker.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.developer_pacemaker.dto.UserDTO;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.UserRepository;

@Service
public class KakaoService {
    @Autowired
    private UserRepository userRepository;

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final String CLIENT_ID = "f245ad8d67e4be9d6c3e5c20f1f8d0a4"; // application.properties? secret.properites?
    private final String REDIRECT_URI = "http://localhost:8080/login/oauth2/code/kakao"; // application.properties? secret.properites?

    public UserDTO kakaoLogin(String code){
        String accessToken = getAccessToken(code);
        UserDTO kakaoUser = getKakaoUserInfo(accessToken);

        if(!userRepository.existsByNickname(kakaoUser.getNickname())) {
            UserEntity userEntity = UserEntity.builder()
                    .email("kakaoLogin")
                    .nickname(kakaoUser.getNickname())
                    .pw(null)
                    .img(null) //  이미지 경로로 수정해야함
                    .social("true")
                    .build();
            userRepository.save(userEntity);
        }
        return kakaoUser;
    }
    private String getAccessToken(String code){
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = KAKAO_TOKEN_URL + "?grant_type=authorization_code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&code=" + code;
        String response = restTemplate.postForObject(requestUrl, null, String.class);
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("access_token");
    }

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
                .email("kakao")
                .nickname(kakaoAccount.getJSONObject("profile").getString("nickname"))
                .pw(null)
                .img(null)
                .social(("true"))
                .build();
    }
}
