package com.example.outsourcing.user.dto;

import com.example.outsourcing.status.Authority;
import lombok.Getter;

@Getter
public class UserRegisterRequestDto {

    private final String email;

    private final String password;

    private final String nickname;

    private final Authority authority;

    public UserRegisterRequestDto(String email, String password, String nickname, String authority) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authority = Authority.valueOf(authority);
    }
}
