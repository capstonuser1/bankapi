package com.example.bankapi.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.api.StatefulRedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Bucket4jConfig {
    private static final Logger log = LoggerFactory.getLogger(Bucket4jConfig.class);

    @Bean
    @ConditionalOnProperty(name = "ratelimiter.redis.enabled", havingValue = "true")
    public ProxyManager<byte[]> proxyManager(@Value("${ratelimiter.redis.uri}") String redisUri) {
        try {
            RedisClient redisClient = RedisClient.create(redisUri);
            StatefulRedisConnection<byte[], byte[]> connection = redisClient.connect(new ByteArrayCodec());
            log.info("Connected to Redis for rate-limiter at {}", redisUri);
            return LettuceBasedProxyManager.builderFor(connection).build();
        } catch (Exception e) {
            String msg = String.format("Failed to create ProxyManager for Bucket4j using Redis URI '%s'. Application configured for distributed rate limiter (ratelimiter.redis.enabled=true); failing fast.", redisUri);
            log.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
    }
}
