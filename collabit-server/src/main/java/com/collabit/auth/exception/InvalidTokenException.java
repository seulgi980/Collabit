package com.collabit.auth.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("로그인 정보가 유효하지 않습니다.");
    }
}
