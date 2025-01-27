package com.collabit.chat.service;

import com.collabit.chat.domain.dto.ChatMessageRequestDTO;
import com.collabit.chat.domain.dto.ChatRoomSwitchDTO;
import com.collabit.chat.domain.dto.ChatUserStatusDTO;
import com.collabit.chat.domain.dto.WebSocketMessageDTO;
import com.collabit.chat.redis.RedisKeyUtil;
import com.collabit.chat.redis.RedisPublisher;
import com.collabit.chat.websocket.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;  // STOMP 메시징 처리
    private final RedisPublisher redisPublisher;  // Redis로 메시지 발행
    private final ChatRedisService chatRedisService;  // Redis 관련 채팅 서비스
    private final ChatRoomDetailService chatRoomDetailService;  // 채팅 메시지 저장 서비스
    private final WebSocketSessionManager webSocketSessionManager;  // WebSocket 세션 관리

    // 채팅 메시지 처리
    public void handleChatMessage(WebSocketMessageDTO messageDTO, String userCode) {
        String channelKey = RedisKeyUtil.getChatMessageChannelKey(messageDTO.getRoomCode());
        redisPublisher.publish(channelKey, messageDTO);

        ChatMessageRequestDTO chatMessage = ChatMessageRequestDTO.builder()
                .roomCode(messageDTO.getRoomCode())
                .message((String) messageDTO.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        chatRoomDetailService.saveMessage(chatMessage, userCode, messageDTO.getRoomCode());

        // STOMP 메시지 브로커에 메시지 전달
        String destination = "/topic/room/" + messageDTO.getRoomCode();
        messagingTemplate.convertAndSend(destination, messageDTO);
        log.info("Message broadcasted to room {}", messageDTO.getRoomCode());
    }

    // 사용자가 채팅방에 입장할 때
    public void handleUserEnter(int roomCode, String userCode) {
        // 사용자 입장 메시지 생성
        ChatUserStatusDTO message = createRoomStatusMessage(userCode, roomCode, true);

        // Redis에 사용자 추가
        chatRedisService.addUserToRoom(roomCode, userCode);

        // 상태 업데이트 발행
        publishRoomStatusMessage(message, roomCode);
    }

    // 사용자가 채팅방을 떠날 때
    public void handleUserExit(int roomCode, String userCode) {
        // 사용자 퇴장 메시지 생성
        ChatUserStatusDTO message = createRoomStatusMessage(userCode, roomCode, false);

        // Redis에서 사용자 제거
        chatRedisService.removeUserFromRoom(roomCode, userCode);

        // 상태 업데이트 발행
        publishRoomStatusMessage(message, roomCode);
    }

    // 방 이동
    public void switchUserRoom(String userCode, ChatRoomSwitchDTO switchDTO) {
        // 새 방 코드
        int newRoomCode = switchDTO.getNewRoomCode();

        // 세션에서 현재 방 코드 가져오기
        int currentRoomCode = webSocketSessionManager.getRoomCodeFromSession(userCode);
        if (currentRoomCode == -1) {
            log.error("Unable to find roomCode for user {}", userCode);
            return;
        }

        // 세션 업데이트
        webSocketSessionManager.switchRoom(userCode, newRoomCode);

        // Redis에서 상태 변경
        redisPublisher.publish(RedisKeyUtil.getRoomStatusChannelKey(), "User " + userCode + " switched rooms.");

        // STOMP 메시지 브로커에 상태 업데이트 전송
        String destination = "/topic/room/" + newRoomCode;
        messagingTemplate.convertAndSend(destination, "User " + userCode + " switched rooms.");
    }

    // 사용자 상태 메시지 생성
    private ChatUserStatusDTO createRoomStatusMessage(String userCode, int roomCode, boolean isOnline) {
        // 입장/퇴장 상태를 나타내는 DTO 객체 생성
        ChatUserStatusDTO message = new ChatUserStatusDTO();
        message.setUserCode(userCode);
        message.setOnline(isOnline);
        message.setRoomCode(roomCode);
        return message;
    }

    // 상태 메시지 Redis와 STOMP로 전송
    private void publishRoomStatusMessage(ChatUserStatusDTO message, int roomCode) {
        // Redis로 상태 업데이트 메시지 발행
        String statusChannelKey = RedisKeyUtil.getRoomStatusChannelKey();
        redisPublisher.publish(statusChannelKey, message);

        // STOMP 브로커로 상태 업데이트 메시지 전송
        String destination = "/topic/room/" + roomCode;
        messagingTemplate.convertAndSend(destination, message);

        log.info("User {} status updated in room {}", message.getUserCode(), roomCode);
    }
}
