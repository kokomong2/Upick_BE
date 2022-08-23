package com.example.upick.login.service;


import com.example.upick.exception.CustomException;
import com.example.upick.exception.ErrorCode;
import com.example.upick.login.dto.SocialUserInfoDto;
import com.example.upick.login.entity.SocialType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthKakaoService implements SocialLoginService {

    @Value("${auth.kakao.client-id}")
    private String kakaoClientKId;

    @Value("${auth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Override
    @Transactional
    public SocialUserInfoDto socialLogin(String code, String state) throws JsonProcessingException {
        String accessToken = getAccessToken(code, state);

        //인증받은 사용자의 정보를 이용하여 SocialUserInfoDto 를 생성하여 반환한다.
        return getUserInfo(accessToken);
    }

    @Override
    public String getAccessToken(String code, String state) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientKId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        try {
            ResponseEntity<String> response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );


            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            String responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String access_token = jsonNode.get("access_token").asText();
            String refresh_token = jsonNode.get("refresh_token").asText();
            System.out.println("access token : " + access_token);
            System.out.println("refresh token : " + refresh_token);

            return access_token;
        } catch (HttpClientErrorException e) {
            throw new CustomException(ErrorCode.COMMON_BAD_REQUEST_400);
        }
    }

    @Override
    public SocialUserInfoDto getUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String username = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String profile_image = jsonNode.get("properties").get("profile_image").asText();

        System.out.println("로그인 이용자 정보");
        System.out.println("카카오 고유ID : " + id);
        System.out.println("닉네임 : " + username);
        System.out.println("이메일 : " + email);
        System.out.println("프로필이미지 URL : " + profile_image);

        return SocialUserInfoDto.builder()
                .email(email)
                .username(username)
                .profileImage(profile_image)
                .social(SocialType.KAKAO).build();
    }
}
