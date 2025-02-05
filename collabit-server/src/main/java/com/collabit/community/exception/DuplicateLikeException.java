package com.collabit.community.exception;

public class DuplicateLikeException extends RuntimeException {
    public DuplicateLikeException() {
        super("중복된 요청입니다.");
    }
}
