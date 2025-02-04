package com.collabit.project.redis;

import com.collabit.project.service.SseEmitterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectRedisSubscriber implements MessageListener {

    private final SseEmitterService sseEmitterService;
    private final RedisTemplate<String, Object> redisTemplate;

    // Redis의 키 이벤트 발생 시 호출되는 메서드, 설문 응답 데이터를 SSE로 전송
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel(), StandardCharsets.UTF_8);

            String[] parts = channel.split(":");
            if (parts.length > 1) {
                String key = parts[1];

                // key 구조 = newSurveyResponse:targetCode:1943e2-d5ae
                if (key != null && key.startsWith("newSurveyResponse:")) {
                    String[] keyParts = key.split(":");
                    if (keyParts.length > 2) {
                        String userCode = keyParts[2];

                        // Redis에서 해당 키의 해시 데이터 조회
                        Map<Object, Object> surveyData = redisTemplate.opsForHash().entries(key);

                        log.debug("설문조사 응답을 받은 유저: {}", userCode);
                        log.debug("설문조사 데이터: {}", surveyData);

                        // SSE를 통해 클라이언트에 데이터 전송
                        sseEmitterService.sendToClient(userCode, surveyData);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Redis 설문 응답 데이터 처리 중 오류가 발생했습니다: {}", e.getMessage(), e);
        }
    }
}
