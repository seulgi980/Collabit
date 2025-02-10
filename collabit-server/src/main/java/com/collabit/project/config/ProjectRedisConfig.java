package com.collabit.project.config;

import com.collabit.project.redis.ProjectRedisSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class ProjectRedisConfig {

    // Redis Subscriber를 메시지 리스너로 등록
    @Bean
    public MessageListenerAdapter listenerAdapter(ProjectRedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    // Redis의 키 이벤트 알림을 구독하기 위한 컨테이너 설정
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("__keyevent@*__:*")); // Redis의 키 이벤트 알림을 구독하기 위한 패턴
        return container;
    }
}
