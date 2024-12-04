package com.example.outsourcing.entity;

import com.example.outsourcing.status.Authority;
import com.example.outsourcing.status.UserStatus;
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

    public User() {
    }
}
