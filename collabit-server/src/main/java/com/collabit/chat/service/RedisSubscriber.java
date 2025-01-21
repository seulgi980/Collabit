package com.collabit.chat.service;

import lombok.extern.slf4j.Slf4j;
import com.collabit.chat.domain.dto.ChatMessagePubDTO;
import com.collabit.chat.domain.dto.ChatMessageSubDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {
    private final RedisTemplate<String, ChatMessagePubDTO> template;
    private final ObjectMapper objectMapper;

    public void sendMessage(String publishMessage) {
        try {
            ChatMessageSubDTO chatMessageSubDTO = objectMapper.readValue(publishMessage, ChatMessageSubDTO.class);
            ChatMessagePubDTO chatMessagePubDTO = ChatMessagePubDTO.builder()
                    .roomCode(chatMessageSubDTO.getRoomCode())
                    .sender(chatMessageSubDTO.getSender())
                    .message(chatMessageSubDTO.getMessage())
                    .time(LocalDateTime.now())
                    .build();
            log.info("받은 메시지: {}", chatMessagePubDTO);
            template.convertAndSend("chat", chatMessagePubDTO);
        } catch (Exception e) {
            log.error("메시지 오류 발생: {}", e.getMessage(), e);
        }
    }
}
