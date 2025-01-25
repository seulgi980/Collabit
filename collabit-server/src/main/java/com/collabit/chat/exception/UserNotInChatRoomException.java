package com.collabit.chat.exception;

public class UserNotInChatRoomException extends RuntimeException {
    public UserNotInChatRoomException() {
        super("사용자가 해당 채팅방에 존재하지 않습니다.");
    }
}
