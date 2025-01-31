package com.collabit.user.exception;

public class UserDifferentException  extends RuntimeException {
    public UserDifferentException() {
        super("유저 정보가 일치하지 않습니다.");
    }
}