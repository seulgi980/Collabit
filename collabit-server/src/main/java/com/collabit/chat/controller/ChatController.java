package com.collabit.chat.controller;

import com.collabit.chat.domain.dto.ChatMessageSubDTO;
import com.collabit.chat.domain.dto.ChatRoomRequestDTO;
import com.collabit.chat.domain.dto.ChatRoomResponseDTO;
import com.collabit.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chatMessage")
    public void receiveMessage(ChatMessageSubDTO chatMessageSubDTO) {
        chatService.processMessage(chatMessageSubDTO);
    }

    @PostMapping("/api/chat/rooms")
    public ResponseEntity<?> createChatRoom(@RequestHeader("Authorization") String token, ChatRoomRequestDTO requestDTO) {
        //token에서 userCode 추출
        String userCode = "1";
        ChatRoomResponseDTO responseDTO = chatService.saveChatRoom(userCode, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
