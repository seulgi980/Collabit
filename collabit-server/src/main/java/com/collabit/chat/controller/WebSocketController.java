package com.collabit.chat.controller;

import com.collabit.chat.domain.dto.ChatRoomSwitchDTO;
import com.collabit.chat.domain.dto.WebSocketMessageDTO;
import com.collabit.chat.exception.MessageContentEmptyException;
import com.collabit.chat.service.ChatRedisService;
import com.collabit.chat.service.WebSocketService;
import com.collabit.global.security.CustomUserDetails;
import com.collabit.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final WebSocketService webSocketService;
    private final ChatRedisService chatRedisService;

    // 연결 설정
    @MessageMapping("/chat.connect/{roomCode}")
    public void connectToRoom(@PathVariable int roomCode, SimpMessageHeaderAccessor headerAccessor) {
        log.debug("Connecting to room {}", roomCode);
        String userCode = getUserCodeFromHeader(headerAccessor);
        // WebSocket 연결 및 상태 업데이트
        webSocketService.handleUserEnter(roomCode, userCode);
        chatRedisService.updateUserStatus(userCode, true);
    }

    // 메시지 전송
    @MessageMapping("/chat.message/{roomCode}")
    public void sendMessageToRoom(WebSocketMessageDTO message, SimpMessageHeaderAccessor headerAccessor) {
        log.debug("Sending message to room {}", message.toString());
        String userCode = getUserCodeFromHeader(headerAccessor);

        // 메시지 유효성 체크
        if (message == null || message.getMessage() == null) {
            log.debug("Message is null or empty");
            throw new MessageContentEmptyException();
        }
        // `handleChatMessage`로 메시지 처리 및 Redis, DB에 저장
        webSocketService.handleChatMessage(message, userCode);
    }

    // 연결 해제
    @MessageMapping("/chat.disconnect/{roomCode}")
    public void disconnect(WebSocketSession session, SimpMessageHeaderAccessor headerAccessor) {
        log.debug("Disconnecting from room {}", session.getId());
        String userCode = getUserCodeFromHeader(headerAccessor);

        // WebSocket 세션에서 roomCode를 가져오기
        int roomCode = (Integer) session.getAttributes().get("roomCode");
        // 사용자 연결 해제 및 상태 업데이트
        webSocketService.handleUserExit(roomCode, userCode);
        chatRedisService.updateUserStatus(userCode, false);
    }

    // 채팅방 이동
    @MessageMapping("/chat.switchRoom")
    public void switchRoom(ChatRoomSwitchDTO switchDTO, SimpMessageHeaderAccessor headerAccessor) {
        log.debug("ChatRoomSwitchDTO {}", switchDTO.toString());
        String userCode = getUserCodeFromHeader(headerAccessor);
        webSocketService.switchUserRoom(userCode, switchDTO);
    }

    private String getUserCodeFromHeader(SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getUser() instanceof Authentication auth) {
            if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
                return userDetails.getCode();
            }
        }
        throw new RuntimeException("인증되지 않은 사용자입니다.");
    }

}