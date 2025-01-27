package com.collabit.chat.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis 채널에 메시지 발행
    public void publish(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
