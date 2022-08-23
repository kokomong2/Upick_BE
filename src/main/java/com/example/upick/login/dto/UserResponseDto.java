package com.example.upick.login.dto;


import com.example.upick.login.entity.RoleType;
import lombok.*;


public class UserResponseDto {

    @Getter
    @Setter
    @Builder
    public static class login{
        private Long id;
        private String username;
        private String email;
        private RoleType roleType;
    }

}
