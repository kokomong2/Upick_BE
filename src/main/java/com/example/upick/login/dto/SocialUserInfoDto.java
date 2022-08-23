package com.example.upick.login.dto;


import com.example.upick.login.entity.SocialType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialUserInfoDto {

    private String email;
    private String username;
    private String profileImage;
    private SocialType social;
}
