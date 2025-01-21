package com.collabit.chat.controller;

import com.collabit.chat.domain.dto.ChatMessageSubDTO;
import com.collabit.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/pub/chatMessage")
    public void receiveMessage(ChatMessageSubDTO chatMessageSubDTO) {
        chatService.processMessage(chatMessageSubDTO);
    }
}
