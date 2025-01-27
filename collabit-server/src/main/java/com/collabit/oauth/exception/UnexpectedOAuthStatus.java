package com.collabit.oauth.exception;

public class UnexpectedOAuthStatus extends RuntimeException {
    public UnexpectedOAuthStatus() {
        super("GitHub 인증 과정에서 예상치 못한 상태가 발생했습니다.");
    }
}