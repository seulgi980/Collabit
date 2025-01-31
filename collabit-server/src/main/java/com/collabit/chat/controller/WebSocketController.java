package com.collabit.chat.controller;

import com.collabit.chat.domain.dto.ChatRoomSwitchDTO;
import com.collabit.chat.domain.dto.WebSocketMessageDTO;
import com.collabit.chat.exception.MessageContentEmptyException;
import com.collabit.chat.service.ChatRedisService;
import com.collabit.chat.service.WebSocketService;
import com.collabit.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.WebSocketSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final WebSocketService webSocketService;
    private final ChatRedisService chatRedisService;

    // 연결 설정
    @MessageMapping("/connect/{roomCode}")
    public void connectToRoom(@PathVariable int roomCode) {
        log.debug("Connecting to room {}", roomCode);
        String userCode = SecurityUtil.getCurrentUserCode();
        // WebSocket 연결 및 상태 업데이트
        webSocketService.handleUserEnter(roomCode, userCode);
        chatRedisService.updateUserStatus(userCode, true);
    }

    // 메시지 전송
    @MessageMapping("/sendMessage/{roomCode}")
    public void sendMessageToRoom(WebSocketMessageDTO message) {
        log.debug("Sending message to room {}", message.toString());
        String userCode = SecurityUtil.getCurrentUserCode();
        // 메시지 유효성 체크
        if (message == null || message.getMessage() == null) {
            log.debug("Message is null or empty");
            throw new MessageContentEmptyException();
        }
        // `handleChatMessage`로 메시지 처리 및 Redis, DB에 저장
        webSocketService.handleChatMessage(message, userCode);
    }

    // 연결 해제
    @MessageMapping("/disconnect")
    public void disconnect(WebSocketSession session) {
        log.debug("Disconnecting from room {}", session.getId());
        String userCode = SecurityUtil.getCurrentUserCode();
        // WebSocket 세션에서 roomCode를 가져오기
        int roomCode = (Integer) session.getAttributes().get("roomCode");
        // 사용자 연결 해제 및 상태 업데이트
        webSocketService.handleUserExit(roomCode, userCode);
        chatRedisService.updateUserStatus(userCode, false);
    }

    // 채팅방 이동
    @MessageMapping("/switchRoom")
    public void switchRoom(ChatRoomSwitchDTO switchDTO) {
        log.debug("ChatRoomSwitchDTO {}", switchDTO.toString());
        String userCode = SecurityUtil.getCurrentUserCode();
        webSocketService.switchUserRoom(userCode, switchDTO);
    }
}