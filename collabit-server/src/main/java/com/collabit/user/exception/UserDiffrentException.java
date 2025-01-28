package com.collabit.user.exception;

public class UserDiffrentException extends RuntimeException {

    public UserDiffrentException() {
        super("유저 정보가 일치하지 않습니다.");
    }
}
