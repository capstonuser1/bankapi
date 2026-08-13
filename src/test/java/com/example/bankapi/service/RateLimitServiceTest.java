package com.example.bankapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimitServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private RateLimitService rateLimiterService;

    @Test
    void isAllowed_enforcesLimit() {
        ValueOperations<String, String> ops = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(ops);
        when(ops.increment(anyString())).thenReturn(1L, 2L, 3L, 4L, 5L, 6L);
        when(stringRedisTemplate.expire(anyString(), any(Duration.class))).thenReturn(true);

        boolean first = rateLimiterService.isAllowed("client1");
        boolean second = rateLimiterService.isAllowed("client1");
        boolean third = rateLimiterService.isAllowed("client1");
        boolean fourth = rateLimiterService.isAllowed("client1");
        boolean fifth = rateLimiterService.isAllowed("client1");
        boolean sixth = rateLimiterService.isAllowed("client1");

        assertTrue(first);
        assertTrue(second);
        assertTrue(third);
        assertTrue(fourth);
        assertTrue(fifth);
        assertFalse(sixth);
    }
}
