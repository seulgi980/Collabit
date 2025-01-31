package com.collabit.chat.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis 채널에 메시지 발행
    public void publish(String channel, Object message) {
        log.debug("Publishing message {} on Channel {}", message.toString(), channel);
        redisTemplate.convertAndSend(channel, message);
    }
}
