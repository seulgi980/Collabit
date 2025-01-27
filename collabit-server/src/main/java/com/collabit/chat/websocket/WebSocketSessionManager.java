package com.collabit.chat.websocket;

import com.collabit.chat.domain.dto.WebSocketMessageDTO;
import com.collabit.chat.service.ChatRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketSessionManager {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRedisService chatRedisService;

    public final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 특정 사용자 세션 가져오기
    public WebSocketSession getSession(String userCode) {
        return sessions.get(userCode);
    }

    // WebSocket 세션에서 roomCode를 얻는 메서드
    public int getRoomCodeFromSession(String userCode) {
        WebSocketSession session = getSession(userCode);
        if (session != null) {
            return (Integer) session.getAttributes().get("roomCode");
        }
        return -1;  // roomCode가 없는 경우 기본값 -1 반환
    }

    // 사용자에게 직접 메시지 전송 (STOMP 방식)
    public void sendDirectMessage(WebSocketMessageDTO messageDTO) {
        String destination = "/topic/room/" + messageDTO.getRoomCode();
        try {
            messagingTemplate.convertAndSend(destination, messageDTO);
        } catch (Exception e) {
            log.error("Failed to send direct message to user: {}", messageDTO.getNickname(), e);
        }
    }

    // 채팅방에 연결된 모든 사용자에게 메시지 전송
    public void broadcastMessage(int roomCode, String messageContent) {
        // 채팅방에 연결된 모든 사용자 가져오기
        List<String> userCodes = chatRedisService.getUsersInRoom(roomCode);

        // 각 사용자에게 메시지 전송 (STOMP 메시지 처리)
        userCodes.forEach(userCode -> {
            String destination = "/topic/room/" + roomCode;
            try {
                WebSocketMessageDTO messageDTO = new WebSocketMessageDTO();
                messageDTO.setMessageType("BROADCAST");
                messageDTO.setMessage(messageContent);
                messagingTemplate.convertAndSend(destination, messageDTO);
            } catch (Exception e) {
                log.error("Failed to broadcast message to room: {}", roomCode, e);
            }
        });
    }

    // 방 이동 처리
    public void switchRoom(String userCode, int newRoomCode) {
        // 이전 방에서 세션 제거
        WebSocketSession session = getSession(userCode);
        if (session != null) {
            int oldRoomCode = getRoomCodeFromSession(userCode);
            session.getAttributes().put("roomCode", newRoomCode);

            // Redis에서 사용자 상태 업데이트
            chatRedisService.removeUserFromRoom(oldRoomCode, userCode);
            chatRedisService.addUserToRoom(newRoomCode, userCode);
        }

        // 새로운 방에 대한 상태 업데이트
        log.info("User {} switched to room {}", userCode, newRoomCode);

        // STOMP로 상태 업데이트 전송
        String destination = "/topic/room/" + newRoomCode;
        messagingTemplate.convertAndSend(destination, "User " + userCode + " switched rooms.");
    }
}
