package com.example.outsourcing.error.errorcode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST



    // 404 NOT_FOUND
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"입력한 유저 ID를 찾을 수 없습니다."),
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "입력한 가게 ID를 찾을 수 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
