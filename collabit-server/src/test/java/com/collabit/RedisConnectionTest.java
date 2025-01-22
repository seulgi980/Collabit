package com.collabit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void redisConnectionTest() {
        String testKey = "testKey";
        String testValue = "testValue";

        redisTemplate.opsForValue().set(testKey, testValue);
        String result = redisTemplate.opsForValue().get(testKey);

        assertThat(result).isEqualTo(testValue);
    }
}
