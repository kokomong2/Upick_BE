package com.example.upick.login.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserRequestDto {

    @Getter
    @Setter
    @Builder
    public static class Signup{

        @NotBlank(message = "아이디는 필수 입력값입니다.")
        private String username;

        @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}",
                message = "올바르지 않은 이메일 형식입니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class Login{

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        private String password;
    }

}
