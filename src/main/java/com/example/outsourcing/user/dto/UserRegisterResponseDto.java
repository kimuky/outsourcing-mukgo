package com.example.outsourcing.user.dto;

import com.example.outsourcing.entity.User;
import com.example.outsourcing.status.Authority;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserRegisterResponseDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final Authority authority;
    private final LocalDateTime createdAt;

    public UserRegisterResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.authority = user.getAuthority();
        this.createdAt = user.getCreatedAt();
    }
}
