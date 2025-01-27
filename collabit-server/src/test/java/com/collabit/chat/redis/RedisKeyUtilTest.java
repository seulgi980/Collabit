package com.collabit.chat.redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisKeyUtilTest {

    @Test
    void testGetRoomUsersKey() {
        String key = RedisKeyUtil.getRoomUsersKey(123);
        assertEquals("room_users:123", key);
    }

    @Test
    void testGetOnlineUsersKey() {
        String key = RedisKeyUtil.getOnlineUsersKey();
        assertEquals("online_users", key);
    }

    @Test
    void testGetChatMessageChannelKey() {
        String key = RedisKeyUtil.getChatMessageChannelKey(123);
        assertEquals("chat_message:123", key);
    }

    @Test
    void testGetRoomStatusChannelKey() {
        String key = RedisKeyUtil.getRoomStatusChannelKey();
        assertEquals("room_status", key);
    }
}
