package com.example.outsourcing.user.service;

import com.example.outsourcing.config.PasswordEncoder;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.user.dto.UserRegisterRequestDto;
import com.example.outsourcing.user.dto.UserRegisterResponseDto;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterResponseDto registerUser(UserRegisterRequestDto requestDto) {

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto, encodedPassword);

        User savedUser = userRepository.save(user);

        return new UserRegisterResponseDto(savedUser);
    }

}
