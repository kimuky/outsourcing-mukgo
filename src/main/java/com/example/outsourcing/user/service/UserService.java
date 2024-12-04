package com.example.outsourcing.user.service;

import com.example.outsourcing.config.PasswordEncoder;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.user.dto.UserLoginRequestDto;
import com.example.outsourcing.user.dto.UserRegisterRequestDto;
import com.example.outsourcing.user.dto.UserRegisterResponseDto;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterResponseDto registerUser(UserRegisterRequestDto requestDto) {

        // 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto, encodedPassword);

        User savedUser = userRepository.save(user);

        return new UserRegisterResponseDto(savedUser);
    }

    public User loginUser(UserLoginRequestDto requestDto) {

        User findUser = userRepository.findUserByEmailOrElseThrow(requestDto.getEmail());

        // 패스워드가 같은지 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return findUser;
    }
}
