package com.collabit.chat.exception;

public class MessageContentEmptyException  extends RuntimeException {
    public MessageContentEmptyException() {
        super("메시지 내용이 비어있습니다. 다시 시도해주세요.");
    }

}
