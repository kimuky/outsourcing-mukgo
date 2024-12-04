package com.example.outsourcing.error.errorcode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // USER
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "중복된 사용자 아이디입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 아이디가 일치하지 않습니다."),
    USER_PASSWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 패스워드가 일치하지 않습니다."),
    USER_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 탈퇴한 사용자 아이디입니다."),


    // 404 NOT_FOUND
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"입력한 유저 ID를 찾을 수 없습니다."),
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "입력한 가게 ID를 찾을 수 없습니다."),


    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),

    // 403 Forbidden
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN,"접근이 거부됐습니다."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND,"리소스를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
