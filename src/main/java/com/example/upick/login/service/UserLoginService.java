package com.example.upick.login.service;


import com.example.upick.exception.CustomException;
import com.example.upick.exception.ErrorCode;
import com.example.upick.login.dto.SocialUserInfoDto;
import com.example.upick.login.dto.UserResponseDto;
import com.example.upick.login.entity.SocialType;
import com.example.upick.login.entity.User;
import com.example.upick.login.repository.UserRepository;
import com.example.upick.login.security.UserDetailsImpl;
import com.example.upick.login.security.jwt.JwtTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final SocialLoginServiceMap socialLoginServiceMap;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserResponseDto.login socialLogin(String social, String code, String state, HttpServletResponse response) throws JsonProcessingException {
        SocialLoginService socialLoginService = socialLoginServiceMap.get(social);
        if(socialLoginService ==null){
            throw new CustomException(ErrorCode.COMMON_BAD_REQUEST_400);
        }

        SocialUserInfoDto socialUserInfoDto = socialLoginService.socialLogin(code,state);
        if(socialUserInfoDto == null){
            throw new CustomException(ErrorCode.COMMON_BAD_REQUEST_400);
        }

        User user = finalLogin(socialUserInfoDto);

        return jwtTokenCreate(user,response);
    }

    public User finalLogin (SocialUserInfoDto userInfoDto){

        System.out.println(userInfoDto.getSocial() + " 유저확인 클래스 들어옴===================");
        String checkEmail = userInfoDto.getEmail();
        System.out.println("이메일 : "+ checkEmail);
        User user = userRepository.findByEmail(checkEmail)
                .orElse(null);
        if (user == null) {
            // 회원가입
            System.out.println("회원정보 없는 회원임(새로운 회원!)");

            // password: random UUID
            String password = UUID.randomUUID().toString();
            System.out.println("비밀번호 생성 : " + password);
            String encodedPassword = passwordEncoder.encode(password);
            System.out.println("비밀번호 암호화  = " + encodedPassword);

            user = new User(userInfoDto,password);
            userRepository.save(user);
        }
        System.out.println("유저정보 넣음");
        System.out.println("user : " +user);
        return user;
    }


    public UserResponseDto.login jwtTokenCreate(User user , HttpServletResponse response) {
        System.out.println("jwtTokenCreate 클래스 들어옴");

        UserDetails userDetails = new UserDetailsImpl(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //여기까진 평범한 로그인과 같음
        System.out.println("강제로그인 시도까지 함");
        //여기부터 토큰 프론트에 넘기는것

        UserDetailsImpl userDetails1 = ((UserDetailsImpl) authentication.getPrincipal());

        System.out.println("userDetails1 : " + userDetails1.toString());

        final String token = JwtTokenUtils.generateJwtToken(userDetails1);

        System.out.println("token값:" + "BEARER " + token);
        response.addHeader("Authorization", "BEARER " + token);

        return UserResponseDto.login.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .roleType(user.getRoleType())
                .build();
    }
}
