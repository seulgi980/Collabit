package com.collabit.chat.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.Mockito.*;

public class RedisPublisherTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private RedisPublisher redisPublisher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublish() {
        // Given
        String channel = "testChannel";
        String message = "Test Message";

        // When
        redisPublisher.publish(channel, message);

        // Then
        verify(redisTemplate, times(1)).convertAndSend(channel, message);
    }
}
