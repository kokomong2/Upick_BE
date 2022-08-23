package com.example.upick.login.service;


import com.example.upick.login.entity.SocialType;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SocialLoginServiceMap extends HashMap<String, SocialLoginService> {

    public SocialLoginServiceMap(AuthGoogleService authGoogleService,
                                 AuthKakaoService authKakaoService,
                                 AuthNaverService authNaverService){
        this.put(SocialType.GOOGLE.getSocial(), authGoogleService);
        this.put(SocialType.NAVER.getSocial(), authNaverService);
        this.put(SocialType.KAKAO.getSocial(), authKakaoService);
    }
}
