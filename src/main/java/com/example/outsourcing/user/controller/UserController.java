package com.example.outsourcing.user.controller;


import com.example.outsourcing.entity.User;
import com.example.outsourcing.user.dto.*;
import com.example.outsourcing.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> registerUser (@Valid @RequestBody UserRegisterRequestDto requestDto) {
        UserRegisterResponseDto registerUser = userService.registerUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> loginUser (@Valid @RequestBody UserLoginRequestDto requestDto,
                                                           HttpServletRequest servletRequest) {
        User loginUser = userService.loginUser(requestDto);

        HttpSession session = servletRequest.getSession();
        session.setAttribute("user", loginUser);

        UserLoginResponseDto loginResponseDto
                = new UserLoginResponseDto(loginUser.getId(), loginUser.getEmail(), "로그인되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDto);
    }
}
