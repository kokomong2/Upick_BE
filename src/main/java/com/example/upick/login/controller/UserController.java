package com.example.upick.login.controller;


import com.example.upick.login.dto.UserRequestDto;
import com.example.upick.login.dto.UserResponseDto;
import com.example.upick.login.entity.SocialType;
import com.example.upick.login.service.UserLoginService;
import com.example.upick.login.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = {"유저 관련 Controller"})
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserLoginService userLoginService;

    @PostMapping("/user/signup")
    public ResponseEntity signupUser(@Valid @RequestBody UserRequestDto.Signup requestDto){
        try{
            return userService.signupUser(requestDto);
        }catch (Exception e){
            return new ResponseEntity("회원가입 실패", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/user/signup/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email){
        return userService.checkEmail(email);
    }

    @PostMapping("oauth/{social}/callback")
    public ResponseEntity<UserResponseDto.login> socialLogin(
            @PathVariable String social,@RequestParam(name="code") String code,
            @RequestParam(name="state") String state,
            HttpServletResponse response) throws JsonProcessingException{
            return new ResponseEntity<>(userLoginService.socialLogin(social,code,state,response),HttpStatus.OK);
    }

}
