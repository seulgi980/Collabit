package com.collabit.chat.controller;

import com.collabit.chat.domain.dto.ChatMessagePubDTO;
import com.collabit.chat.domain.dto.ChatMessageSubDTO;
import com.collabit.chat.domain.dto.ChatRoomRequestDTO;
import com.collabit.chat.domain.dto.ChatRoomResponseDTO;
import com.collabit.chat.service.ChatMessageService;
import com.collabit.chat.service.ChatRoomService;
import com.collabit.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chatMessage")
    public void receiveMessage(ChatMessageSubDTO chatMessageSubDTO) {
        chatService.processMessage(chatMessageSubDTO);
    }

    @PostMapping("/api/chat/rooms")
    public ResponseEntity<?> createChatRoom(@RequestHeader("Authorization") String token, ChatRoomRequestDTO requestDTO) {
        //token에서 userCode 추출
        String userCode = "1";
        ChatRoomResponseDTO responseDTO = chatRoomService.saveChatRoom(userCode, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/api/chat/rooms/{roomCode}/message")
    public ResponseEntity<?> getMessages(@RequestHeader("Authorization") String token, @PathVariable int roomCode) {
        //token에서 userCode 추출
        String userCode = "1";
        List<ChatMessagePubDTO> responseDTO = chatMessageService.getMessages(userCode, roomCode);
        if (responseDTO.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(responseDTO);
    }
}
