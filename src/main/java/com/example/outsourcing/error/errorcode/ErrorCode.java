package com.example.outsourcing.error.errorcode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    USER_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 탈퇴한 사용자 아이디입니다."),
    INVALID_RATING(HttpStatus.BAD_REQUEST,"별점은 1점 ~ 5점 사이여야합니다."),
    NOT_CREATE_STORE(HttpStatus.BAD_REQUEST, "개인당 가게는 최대 3개입니다."),
    OPEN_AND_CLOSE(HttpStatus.BAD_REQUEST,"마감시간이 오픈시간보다 빠를 순 없습니다."),
    BAD_REQUEST_YEAR_MONTH(HttpStatus.BAD_REQUEST,"연도 혹은 달을 잘못 입력하셨습니다."),
    
    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),


    // 403 Forbidden
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN,"접근이 거부됐습니다."),
    FORBIDDEN_PERMISSION(HttpStatus.FORBIDDEN, "사용자 권한이 없습니다."),
    FORBIDDEN_OWNER(HttpStatus.FORBIDDEN,"본인의 가게가 아닙니다."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND,"리소스를 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게 ID를 찾을 수 없습니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND,"메뉴 ID를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"메뉴 ID를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 아이디가 일치하지 않습니다."),
    USER_PASSWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 패스워드가 일치하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"USER_NOT_FOUND로 수정해주세요"),

    // 409 CONFLICT
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "중복된 아이디입니다."),
    USER_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 탈퇴한 사용자 아이디입니다.")

    ;


    private final HttpStatus httpStatus;
    private final String detail;
}
