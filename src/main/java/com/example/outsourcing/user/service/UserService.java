package com.example.outsourcing.user.service;

import com.example.outsourcing.config.PasswordEncoder;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.status.UserStatus;
import com.example.outsourcing.user.dto.UserLoginRequestDto;
import com.example.outsourcing.user.dto.UserPasswordRequestDto;
import com.example.outsourcing.user.dto.UserRegisterRequestDto;
import com.example.outsourcing.user.dto.UserRegisterResponseDto;
import com.example.outsourcing.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterResponseDto registerUser(UserRegisterRequestDto requestDto) {

        List<User> users
                = userRepository.findUserByEmailAndStatus(requestDto.getEmail(), UserStatus.DEACTIVATED);

        if (!users.isEmpty()) {
            throw new CustomException(ErrorCode.FORBIDDEN_REGISTER);
        }

        // 이메일 중복 검사
        userRepository.findUserByEmailForDuplicateCheck(requestDto.getEmail());

        // 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto, encodedPassword);

        User savedUser = userRepository.save(user);

        return new UserRegisterResponseDto(savedUser);
    }

    public User loginUser(UserLoginRequestDto requestDto) {

        User findUser = userRepository.findUserByEmailOrElseThrow(requestDto.getEmail());

        if (findUser.getStatus().equals(UserStatus.DEACTIVATED)) {
            throw new CustomException(ErrorCode.FORBIDDEN_LOGIN);
        }

        // 패스워드 일치 여부 검사
        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        return findUser;
    }

    @Transactional
    public void deleteUser(Long userId, @Valid UserPasswordRequestDto requestDto) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (findUser.getStatus().equals(UserStatus.DEACTIVATED)) {
            throw new CustomException(ErrorCode.FORBIDDEN_LOGIN);
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        findUser.updateDeactivatedStatus();
    }
}


