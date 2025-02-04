package com.collabit.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "올바르지 않은 타입입니다");

    // Business

    private final HttpStatus status;
    private final String message;
}
