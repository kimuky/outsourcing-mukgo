package com.example.outsourcing.user.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;

    private final String email;

    private final String message;

    public UserResponseDto(Long id, String email, String message) {
        this.id = id;
        this.email = email;
        this.message = message;
    }
}
