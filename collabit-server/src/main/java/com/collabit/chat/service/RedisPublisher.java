package com.collabit.chat.service;

import com.collabit.chat.domain.dto.ChatMessageSubDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChatMessageSubDTO message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}