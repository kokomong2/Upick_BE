package com.example.upick.login.service;


import com.example.upick.login.dto.SocialUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public class AuthGoogleService implements SocialLoginService{


    @Override
    public SocialUserInfoDto socialLogin(String code, String state) throws JsonProcessingException {
        return null;
    }

    @Override
    public String getAccessToken(String code, String state) throws JsonProcessingException {
        return null;
    }

    @Override
    public SocialUserInfoDto getUserInfo(String accessToken) throws JsonProcessingException {
        return null;
    }
}
