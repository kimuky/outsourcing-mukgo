package com.example.outsourcing.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private final String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 영문/숫자/특수문자를 포함해야 합니다.")
    private final String password;
    
    public UserLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
