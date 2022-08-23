package com.example.upick.login.entity;


import com.example.upick.login.dto.SocialUserInfoDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "username","email","roleType"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileImg;

    private SocialType socialType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;


    public User(SocialUserInfoDto socialUserInfoDto,String password){
        this.email = socialUserInfoDto.getEmail();
        this.username = socialUserInfoDto.getUsername();
        this.profileImg = socialUserInfoDto.getProfileImage();
        this.socialType = socialUserInfoDto.getSocial();
        this.password = password;
        this.roleType = RoleType.USER;
    }

}
