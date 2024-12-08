package com.example.outsourcing.entity;

import com.example.outsourcing.status.Authority;
import com.example.outsourcing.status.UserStatus;
import com.example.outsourcing.user.dto.UserRegisterRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public User(UserRegisterRequestDto requestDto, String encodedPassword) {
        this.email = requestDto.getEmail();
        this.password = encodedPassword;
        this.nickname = requestDto.getNickname();
        this.status = UserStatus.ACTIVATED;
        this.authority = requestDto.getAuthority();
    }

    public User() {
    }

    // 회원 탈퇴 상태로 변경
    public void updateDeactivatedStatus() {
        this.status = UserStatus.DEACTIVATED;
    }
}
