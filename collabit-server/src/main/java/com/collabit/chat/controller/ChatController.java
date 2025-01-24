package com.collabit.chat.controller;

import com.collabit.chat.domain.dto.*;
import com.collabit.chat.service.ChatRoomDetailService;
import com.collabit.chat.service.ChatRoomListService;
import com.collabit.chat.service.ChatService;
import com.collabit.global.common.PageResponseDTO;
import com.collabit.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomListService chatRoomListService;
    private final ChatRoomDetailService chatRoomDetailService;

    @PostMapping("/room")
    public ResponseEntity<?> getOrCreateChatRoom(@RequestBody ChatRoomRequestDTO requestDTO, @RequestHeader("Authorization") String token) {
        String userCode = SecurityUtil.getCurrentUserId();
        ChatRoomResponseDTO responseDTO = chatRoomListService.saveChatRoom(userCode, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/room")
    public ResponseEntity<?> getChatRoomList(@RequestBody ChatRoomListRequestDTO requestDTO, @RequestHeader("Authorization") String token) {
//      String userCode = securityUtil.getCurrentUserId(token);
        String userCode = "1";
        PageResponseDTO responseDTO = chatRoomListService.getChatRoomList(userCode, requestDTO);
        if (((List<?>) responseDTO.getContent()).isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/room/{roomCode}")
    public ResponseEntity<?> getChatMessages(@RequestBody ChatRoomDetailRequestDTO requestDTO, @RequestHeader("Authorization") String token, @PathVariable int roomCode) {
//      String userCode = securityUtil.getCurrentUserId(token);
        String userCode = "1";
        PageResponseDTO responseDTO = chatRoomDetailService.getChatRoomDetail(userCode, requestDTO);
        if (((ChatRoomDetailResponseDTO) responseDTO.getContent()).getMessages().isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadMessages(@RequestHeader("Authorization") String token) {
//      String userCode = securityUtil.getCurrentUserId(token);
        String userCode = "1";
        ChatUnreadResponseDTO responseDTO = chatRoomListService.getUnreadMessagesForUser(userCode);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<?> getChatRoomWithNickname(@RequestHeader("Authorization") String token, @RequestParam String nickname) {
//      String userCode = securityUtil.getCurrentUserId(token);
        String userCode = "1";
        ChatRoomResponseDTO responseDTO = chatRoomListService.getChatRoomByNickname(userCode, nickname);
        if (responseDTO.getRoomCode() == 0) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(responseDTO);
    }
}
