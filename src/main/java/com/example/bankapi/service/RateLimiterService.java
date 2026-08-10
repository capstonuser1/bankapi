package com.example.bankapi.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {
    private final StringRedisTemplate stringRedisTemplate;

    public RateLimiterService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean isAllowed(String clientId) {
        int maxRequests = 5;
        Duration window = Duration.ofSeconds(60);

        String key = "rate_limiter:" + clientId;

        Long currentCount = stringRedisTemplate.opsForValue().increment(key);

        if(currentCount == null) {
            return true;
        }

        if(currentCount == 1) {
            stringRedisTemplate.expire(key, window);
        }

        return currentCount <= maxRequests;
    }
}
