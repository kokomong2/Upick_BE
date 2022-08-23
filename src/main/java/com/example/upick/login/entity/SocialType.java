package com.example.upick.login.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    KAKAO("kakao"),
    GOOGLE("google"),
    NAVER("naver");

    private final String social;
}
