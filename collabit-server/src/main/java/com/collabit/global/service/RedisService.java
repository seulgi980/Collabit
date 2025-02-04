package com.collabit.global.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 특정 userCode에 대한 모든 newSurveyResponse 키-값 쌍을 조회
    public Map<Integer, Boolean> findNewSurveyResponsesByUserCode(String userCode) {
        try {
            // newSurveyResponse:{userCode}:* 패턴으로 Redis에서 해당 유저와 관련된 모든 키 조회
            String pattern = "newSurveyResponse:" + userCode + ":*";
            Set<String> keys = redisTemplate.keys(pattern);

            if (keys == null || keys.isEmpty()) {
                return new HashMap<>();
            }

            // projectInfoCode를 key로, true를 value로 하는 Map 생성
            Map<Integer, Boolean> projectInfoCodeMap = new HashMap<>();

            for (String key : keys) {
                String[] keyParts = key.split(":");
                if (keyParts.length == 3) {
                    try {
                        // projectInfoCode 추출 후 Map(key 기준 중복x)에 저장
                        int projectInfoCode = Integer.parseInt(keyParts[2]);

                        // 해당 projectInfoCode는 읽지 않은 알림이 있는 것이므로 true
                        projectInfoCodeMap.put(projectInfoCode, true);
                    } catch (NumberFormatException e) {
                        log.warn("Invalid projectInfoCode in Redis key: {}", key);
                        throw new RuntimeException("프로젝트 정보 코드가 올바르지 않습니다: ");
                    }
                }
            }
            return projectInfoCodeMap;
        } catch (Exception e) {
            log.error("Redis에서 newSurveyResponse 조회 중 오류 발생", e);
            return new HashMap<>();
        }
    }
}
