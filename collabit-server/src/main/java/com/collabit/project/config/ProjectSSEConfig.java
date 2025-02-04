package com.collabit.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ProjectSSEConfig {

    // SSE 연결을 관리하기 위한 ConcurrentHashMap 빈 등록
    @Bean
    public ConcurrentHashMap<String, SseEmitter> sseEmitters() {
        return new ConcurrentHashMap<>();
    }
}
