package com.example.outsourcing.error.errorcode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),

    // 403 Forbidden
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN,"접근이 거부됐습니다."),

    // 404 NOT_FOUND
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"입력한 유저 ID를 찾을 수 없습니다.");



    private final HttpStatus httpStatus;
    private final String detail;
}
