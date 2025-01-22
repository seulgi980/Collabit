package com.collabit.chat.service;

import com.collabit.chat.domain.dto.ChatMessagePubDTO;
import com.collabit.chat.domain.dto.ChatMessageSubDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    // 메시지 처리 및 브로드캐스트
    public void processMessage(ChatMessageSubDTO chatMessageSubDTO) {
        ChatMessagePubDTO chatMessagePubDTO = ChatMessagePubDTO.builder()
                .roomCode(chatMessageSubDTO.getRoomCode())
                .sender(chatMessageSubDTO.getSender())
                .message(chatMessageSubDTO.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        simpMessagingTemplate.convertAndSend("/sub/chatRoom/" + chatMessageSubDTO.getRoomCode(), chatMessagePubDTO);
    }
}
