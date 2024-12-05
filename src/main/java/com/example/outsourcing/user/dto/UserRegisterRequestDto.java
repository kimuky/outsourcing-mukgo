package com.example.outsourcing.user.dto;

import com.example.outsourcing.status.Authority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRegisterRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @NotBlank(message = "아이디는 이메일 형식이어야 합니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private final String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private final String nickname;

    private final Authority authority;

    public UserRegisterRequestDto(String email, String password, String nickname, String authority) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authority = Authority.valueOf(authority);
    }
}
