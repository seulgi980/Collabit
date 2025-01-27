package com.collabit.chat.service;

import com.collabit.chat.redis.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 채팅방에 사용자 추가
    public void addUserToRoom(int roomCode, String userCode) {
        String key = RedisKeyUtil.getRoomUsersKey(roomCode);
        redisTemplate.opsForSet().add(key, userCode);
    }

    // 채팅방에서 사용자 제거
    public void removeUserFromRoom(int roomCode, String userCode) {
        String key = RedisKeyUtil.getRoomUsersKey(roomCode);
        redisTemplate.opsForSet().remove(key, userCode);
    }

    // 특정 채팅방 사용자 목록 조회
    public List<String> getUsersInRoom(int roomCode) {
        String key = RedisKeyUtil.getRoomUsersKey(roomCode);
        return redisTemplate.opsForSet().members(key).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    // 사용자 상태 업데이트
    public void updateUserStatus(String userCode, boolean isOnline) {
        String key = RedisKeyUtil.getOnlineUsersKey();
        if (isOnline) {
            redisTemplate.opsForSet().add(key, userCode);
        } else {
            redisTemplate.opsForSet().remove(key, userCode);
        }
    }

    // 읽지 않은 메시지 수 반환
    public int getUnreadMessagesCount(int roomCode, String userCode) {
        String key = RedisKeyUtil.getChatMessageChannelKey(roomCode);
        Object unreadCount = redisTemplate.opsForHash().get(key, userCode);
        return unreadCount != null ? Integer.parseInt(unreadCount.toString()) : 0;
    }

    // 모든 읽지 않은 메시지 수 반환 (사용자 기준)
    public int getUnreadMessagesForUser(String userCode) {
        String key = RedisKeyUtil.getOnlineUsersKey();
        // Redis에 저장된 사용자 기준 unread count를 계산
        return redisTemplate.opsForHash().entries(key).values().stream()
                .mapToInt(value -> Integer.parseInt(value.toString()))
                .sum();
    }

    // 메시지 상태 업데이트
    public void updateRoomMessageStatus(int roomCode, String userCode, boolean isRead) {
        String key = RedisKeyUtil.getChatMessageChannelKey(roomCode);
        if (isRead) {
            redisTemplate.opsForHash().delete(key, userCode);
        } else {
            // Redis에 저장된 읽지 않은 메시지 상태를 업데이트
            redisTemplate.opsForHash().increment(key, userCode, 1);
        }
    }
}
