package com.collabit.chat.redis;

import com.collabit.chat.domain.dto.WebSocketMessageDTO;
import com.collabit.chat.websocket.WebSocketSessionManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final WebSocketSessionManager sessionManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);

            WebSocketMessageDTO webSocketMessage = objectMapper.readValue(body, WebSocketMessageDTO.class);
            log.error("Received message Body {}", body);
            log.error("Received web socket message: {}", webSocketMessage);

            // 메시지 유형에 따라 처리 (1:1 채팅만 처리)
            if ("BROADCAST".equals(webSocketMessage.getMessageType())) {
                sessionManager.broadcastMessage(
                        webSocketMessage.getRoomCode(),
                        objectMapper.writeValueAsString(webSocketMessage)
                );
            } else {
                log.warn("처리할 수 없는 메시지 타입: {}", webSocketMessage.getMessageType());
            }
        } catch (Exception e) {
            log.error("레디스 메시지 전송에 실패했습니다. {}", e.getMessage(), e);
        }
    }
}
