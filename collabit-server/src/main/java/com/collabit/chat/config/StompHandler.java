package com.collabit.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    // 인증/인가 로직 추가 필요
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            String token = accessor.getFirstNativeHeader("Authorization");
//            if (token == null || !isValidToken(token)) {
//                throw new IllegalArgumentException("Invalid Token");
//            }
//        }
        return message;
    }
}
