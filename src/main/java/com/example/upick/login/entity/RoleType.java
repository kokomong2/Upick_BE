package com.example.upick.login.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER","일반 사용자 권한"),
    PHARMACIST("ROLE_PHARMACIST", "약사 사용자 권한"),
    GUEST("GUEST", "게스트 권한" );
    
    private final String code;
    private final String displayName;
    
    public static RoleType of(String code){
        return Arrays.stream(RoleType.values())
                .filter(r-> r.getCode().equals(code))
                .findAny()
                .orElse(GUEST);
    }
}
