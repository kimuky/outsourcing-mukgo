package com.example.outsourcing.user.service;

import com.example.outsourcing.config.PasswordEncoder;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.user.dto.UserLoginRequestDto;
import com.example.outsourcing.user.dto.UserRegisterRequestDto;
import com.example.outsourcing.user.dto.UserRegisterResponseDto;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //이메일 형식 정규식
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public UserRegisterResponseDto registerUser(UserRegisterRequestDto requestDto) {

        // 이메일 중복 검사
        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }

        // 이메일 형식 검사
        if (!Pattern.matches(EMAIL_REGEX, requestDto.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_EMAIL);
        }

        // 비밀번호 형식 검사
        if (!Pattern.matches(PASSWORD_REGEX, requestDto.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

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
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return findUser;
    }
}
