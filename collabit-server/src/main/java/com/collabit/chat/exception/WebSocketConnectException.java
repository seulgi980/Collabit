package com.collabit.chat.exception;

public class WebSocketConnectException extends RuntimeException {
    public WebSocketConnectException(String message) {super("실시간 채팅 연결에 문제가 발생했습니다.");}
}
