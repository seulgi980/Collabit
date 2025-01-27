package com.collabit.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;

//    //channelTopic에 메시지 발행
//    public void publish(ChannelTopic topic, WebSocketMessageDTO message) {
//        redisTemplate.convertAndSend(topic.getTopic(), message);
//    }
}
