package com.example.upick.login.service;

import com.example.upick.login.dto.UserRequestDto;
import com.example.upick.login.entity.RoleType;
import com.example.upick.login.entity.User;
import com.example.upick.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ResponseEntity<String> signupUser(UserRequestDto.Signup requestDto) {

        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(password)
                .roleType(RoleType.USER)
                .build();
        userRepository.save(user);
        System.out.println("User : " +user);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

    public ResponseEntity<Boolean> checkEmail(String email) {
        Boolean isEmail = userRepository.findByEmail(email).isPresent();
        return new ResponseEntity<>(isEmail,HttpStatus.OK);
    }
}
