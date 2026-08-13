package com.example.bankapi.service;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    private static final Logger log = LoggerFactory.getLogger(RateLimitService.class);

    private final ProxyManager<byte[]> proxyManager; // may be null when Redis not configured/available
    private final Map<String, InMemoryBucket> localBuckets = new ConcurrentHashMap<>();
    private static final int CAPACITY = 2;
    private static final Duration REFILL_PERIOD = Duration.ofMinutes(1);

    @Autowired
    public RateLimitService(Optional<ProxyManager<byte[]>> proxyManagerOpt) {
        this.proxyManager = proxyManagerOpt.orElse(null);
        if (this.proxyManager != null) {
            log.info("RateLimitService: using distributed ProxyManager implementation: {}", this.proxyManager.getClass().getName());
        } else {
            log.info("RateLimitService: no ProxyManager bean found; falling back to in-memory rate limiter");
        }
    }

    // Returns remaining tokens if consumed, or -1 if not allowed (rate limited)
    public synchronized long tryConsume(String clientId, int tokens) {
        if (proxyManager != null) {
            // distributed mode: use ProxyManager builder to create a Bucket and consume from it
            byte[] key = clientId.getBytes();
            // lazy retrieval each call to avoid keeping Bucket references
            try {
                var bucket = proxyManager.builder().build(key, this::createBucketConfig);
                var probe = bucket.tryConsumeAndReturnRemaining(tokens);
                return probe.isConsumed() ? probe.getRemainingTokens() : -1L;
            } catch (Exception e) {
                // If distributed manager fails at runtime, fall back to local in-memory behavior
            }
        }
        // In-memory fallback
        InMemoryBucket bucket = localBuckets.computeIfAbsent(clientId, id -> new InMemoryBucket(CAPACITY, REFILL_PERIOD));
        return bucket.tryConsume(tokens);
    }

    private Bandwidth createBandwidth() {
        return Bandwidth.builder()
                .capacity(CAPACITY)
                .refillGreedy(CAPACITY, REFILL_PERIOD)
                .build();
    }

    public BucketConfiguration createBucketConfig() {
        return BucketConfiguration.builder().addLimit(createBandwidth()).build();
    }

    // Compatibility helper used by older tests and callers
    public boolean isAllowed(String clientId) {
        return tryConsume(clientId, 1) >= 0;
    }

    // Simple token-bucket replacement for local fallback
    private static class InMemoryBucket {
        private int tokens;
        private long lastRefillMillis;
        private final int capacity;
        private final Duration refillPeriod;

        InMemoryBucket(int capacity, Duration refillPeriod) {
            this.capacity = capacity;
            this.refillPeriod = refillPeriod;
            this.tokens = capacity;
            this.lastRefillMillis = System.currentTimeMillis();
        }

        synchronized long tryConsume(int requested) {
            refillIfNeeded();
            if (tokens >= requested) {
                tokens -= requested;
                return tokens;
            }
            return -1L;
        }

        private void refillIfNeeded() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefillMillis;
            long periodMillis = refillPeriod.toMillis();
            if (elapsed >= periodMillis) {
                tokens = capacity;
                lastRefillMillis = now;
            }
        }
    }
}
