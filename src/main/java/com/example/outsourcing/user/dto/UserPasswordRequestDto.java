package com.example.outsourcing.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPasswordRequestDto {


    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 영문/숫자/특수문자를 포함해야 합니다.")
    private final String password;

    @JsonCreator
    public UserPasswordRequestDto(String password) {
        this.password = password;
    }
}
