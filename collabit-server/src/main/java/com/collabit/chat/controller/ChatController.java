package com.collabit.chat.controller;

import com.collabit.chat.domain.dto.*;
import com.collabit.chat.service.ChatRoomDetailService;
import com.collabit.chat.service.ChatRoomListService;
import com.collabit.global.common.PageResponseDTO;
import com.collabit.global.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ChatController", description = "채팅 API")
@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("/api/chat")
@Slf4j
public class ChatController {

    private final ChatRoomListService chatRoomListService;
    private final ChatRoomDetailService chatRoomDetailService;

    @Operation(summary = "채팅방 생성 또는 조회", description = "채팅방을 생성하거나 존재하는 채팅방을 조회합니다.")
    @PostMapping("/room")
    public ResponseEntity<?> getOrCreateChatRoom(@RequestBody ChatRoomRequestDTO requestDTO) {
        log.debug("getOrCreateChatRoom requestDTO: {}", requestDTO.toString());
        String userCode = SecurityUtil.getCurrentUserCode();
        ChatRoomResponseDTO responseDTO = chatRoomListService.saveChatRoom(userCode, requestDTO);
        log.debug("getOrCreateChatRoom responseDTO: {}", responseDTO.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "채팅방 목록 조회", description = "사용자의 채팅방 목록을 조회합니다.")
    @PostMapping("/room/list")
    public ResponseEntity<?> getChatRoomList(@RequestBody ChatRoomListRequestDTO requestDTO) {
        log.debug("getChatRoomList requestDTO: {}", requestDTO.toString());
        String userCode = SecurityUtil.getCurrentUserCode();
        PageResponseDTO responseDTO = chatRoomListService.getChatRoomList(userCode, requestDTO);
        log.debug("getChatRoomList responseDTO: {}", responseDTO.toString());
        if (((List<?>) responseDTO.getContent()).isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "채팅방 메시지 조회", description = "채팅방의 메시지를 조회합니다.")
    @GetMapping("/room/{roomCode}")
    public ResponseEntity<?> getChatMessages(@RequestBody ChatRoomDetailRequestDTO requestDTO, @PathVariable int roomCode) {
        log.debug("getChatMessages requestDTO: {}", requestDTO.toString());
        String userCode = SecurityUtil.getCurrentUserCode();
        PageResponseDTO responseDTO = chatRoomDetailService.getChatRoomDetail(userCode, requestDTO);
        log.debug("getChatMessages responseDTO: {}", responseDTO.toString());
        if (((ChatRoomDetailResponseDTO) responseDTO.getContent()).getMessages().isEmpty())
            return ResponseEntity.noContent().build();
        chatRoomDetailService.markMessagesAsRead(roomCode, userCode);
        log.debug("markMessagesAsRead roomCode: {}", roomCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "읽지 않은 메시지 조회", description = "사용자가 읽지 않은 메시지 수를 조회합니다.")
    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadMessages() {
        String userCode = SecurityUtil.getCurrentUserCode();
        ChatUnreadResponseDTO responseDTO = chatRoomListService.getUnreadMessagesCount(userCode);
        log.debug("getUnreadMessages responseDTO: {}", responseDTO.toString());
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "닉네임으로 채팅방 조회", description = "닉네임을 기반으로 채팅방을 조회합니다.")
    @PostMapping("/room/nickname")
    public ResponseEntity<?> getChatRoomWithNickname(ChatRoomRequestDTO requestDTO) {
        log.debug("getChatRoomWithNickname requestDTO: {}", requestDTO.toString());
        String userCode = SecurityUtil.getCurrentUserCode();
        ChatRoomResponseDTO responseDTO = chatRoomListService.getChatRoomByNickname(userCode, requestDTO);
        log.debug("getChatRoomWithNickname responseDTO: {}", responseDTO.toString());
        if (responseDTO.getRoomCode() == 0) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(responseDTO);
    }
}
