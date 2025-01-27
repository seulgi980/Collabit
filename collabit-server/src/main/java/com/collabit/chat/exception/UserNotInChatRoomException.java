package com.collabit.chat.exception;

public class UserNotInChatRoomException extends RuntimeException {
    public UserNotInChatRoomException() {
        super("채팅방에 참여하지 않은 상태에서는 메시지를 보낼 수 없습니다. 채팅방에 참여한 후 다시 시도해 주세요.");
    }
}
