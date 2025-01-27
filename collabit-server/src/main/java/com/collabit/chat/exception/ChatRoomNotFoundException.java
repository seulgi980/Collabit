package com.collabit.chat.exception;

public class ChatRoomNotFoundException extends RuntimeException {
    public ChatRoomNotFoundException() {
        super("해당 채팅방을 찾을 수 없습니다.");
    }
}
