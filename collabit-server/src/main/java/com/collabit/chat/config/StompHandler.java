package com.collabit.chat.config;

import com.collabit.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 명령어인 경우 인증 로직 수행
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String userCode = SecurityUtil.getCurrentUserCode();
            log.debug("Connected to user {}", userCode);
            accessor.setUser(() -> userCode); // WebSocket 세션에 사용자 정보 설정
        }

        return message;
    }
}
