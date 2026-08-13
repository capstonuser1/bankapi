package com.example.bankapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class RateLimiterServiceIntegrationTest {

    @Autowired
    private RateLimiterService rateLimiterService;

    @MockBean
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void isAllowed_usesRedisOps() {
        ValueOperations<String, String> ops = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(ops);
        when(ops.increment(anyString())).thenReturn(1L, 2L, 3L, 4L, 5L, 6L);
        when(stringRedisTemplate.expire(anyString(), org.mockito.ArgumentMatchers.any())).thenReturn(true);

        assertTrue(rateLimiterService.isAllowed("client-x"));
        assertTrue(rateLimiterService.isAllowed("client-x"));
        assertTrue(rateLimiterService.isAllowed("client-x"));
        assertTrue(rateLimiterService.isAllowed("client-x"));
        assertTrue(rateLimiterService.isAllowed("client-x"));
        assertFalse(rateLimiterService.isAllowed("client-x"));
    }
}
