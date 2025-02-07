package com.collabit.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // =========== Common ===========
    // 400 Bad Request (잘못된 형식)
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "올바르지 않은 타입입니다"),
    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "필수 입력값이 누락되었습니다."),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "요청 데이터가 형식이 올바르지 않습니다."),

    // 401 Unauthorized (인증 실패)
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),

    // 403 Forbidden (인가 실패)
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404 Not Found
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다."),

    // 500 Internal Server Error (예상하지 못한 예외)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // =========== Auth ===========
    // 400 Bad Request (잘못된 형식)
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증코드가 틀립니다."),

    // 409 Conflict (중복)
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 닉네임입니다."),

    // =========== portfolio ===========
    INVALID_PARTICIPANT_COUNT(HttpStatus.BAD_REQUEST, "참여자 수가 0명이라 평균 계산이 불가능합니다.");

    // =========== Business ===========

    private final HttpStatus status;
    private final String message;
}
