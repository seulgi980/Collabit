package com.collabit.chat.service;

import com.collabit.chat.redis.RedisKeyUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ChatRedisServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private SetOperations<String, Object> setOperations;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private ChatRedisService chatRedisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    @Test
    void testAddUserToRoom() {
        // Given
        int roomCode = 123;
        String userCode = "user1";

        // When
        chatRedisService.addUserToRoom(roomCode, userCode);

        // Then
        verify(redisTemplate.opsForSet(), times(1)).add(RedisKeyUtil.getRoomUsersKey(roomCode), userCode);
    }

    @Test
    void testRemoveUserFromRoom() {
        // Given
        int roomCode = 123;
        String userCode = "user1";

        // When
        chatRedisService.removeUserFromRoom(roomCode, userCode);

        // Then
        verify(redisTemplate.opsForSet(), times(1)).remove(RedisKeyUtil.getRoomUsersKey(roomCode), userCode);
    }

    @Test
    void testGetUsersInRoom() {
        // Given
        int roomCode = 123;
        String userCode = "user1";
        String anotherUserCode = "user2";

        when(redisTemplate.opsForSet().members(RedisKeyUtil.getRoomUsersKey(roomCode))).thenReturn(Set.of(userCode, anotherUserCode));

        // When
        List<String> usersInRoom = chatRedisService.getUsersInRoom(roomCode);

        // Then
        assertNotNull(usersInRoom);
        assertEquals(2, usersInRoom.size());
        assertTrue(usersInRoom.contains(userCode));
        assertTrue(usersInRoom.contains(anotherUserCode));
    }

    @Test
    void testUpdateUserStatus() {
        // Given
        String userCode = "user1";
        boolean isOnline = true;

        // When
        chatRedisService.updateUserStatus(userCode, isOnline);

        // Then
        verify(redisTemplate.opsForSet(), times(1)).add(RedisKeyUtil.getOnlineUsersKey(), userCode);
    }

    @Test
    void testGetUnreadMessagesCount() {
        // Given
        int roomCode = 123;
        String userCode = "user1";
        String key = RedisKeyUtil.getChatMessageChannelKey(roomCode);
        when(redisTemplate.opsForHash().get(key, userCode)).thenReturn(5);

        // When
        int unreadCount = chatRedisService.getUnreadMessagesCount(roomCode, userCode);

        // Then
        assertEquals(5, unreadCount);
    }

    @Test
    void testGetUnreadMessagesForUser() {
        // Given
        String userCode = "user1";
        String key = RedisKeyUtil.getOnlineUsersKey();
        when(redisTemplate.opsForHash().entries(key)).thenReturn(Map.of(userCode, 5));

        // When
        int totalUnreadCount = chatRedisService.getUnreadMessagesForUser(userCode);

        // Then
        assertEquals(5, totalUnreadCount);
    }

    @Test
    void testUpdateRoomMessageStatus() {
        // Given
        int roomCode = 123;
        String userCode = "user1";
        boolean isRead = true;
        String key = RedisKeyUtil.getChatMessageChannelKey(roomCode);

        // When
        chatRedisService.updateRoomMessageStatus(roomCode, userCode, isRead);

        // Then
        if (isRead) {
            verify(redisTemplate.opsForHash(), times(1)).delete(key, userCode);  // Delete unread count for the user
        } else {
            verify(redisTemplate.opsForHash(), times(1)).increment(key, userCode, 1);  // Increment unread count for the user
        }
    }
}
