package com.example.bankapi.service;

import org.springframework.stereotype.Service;

/**
 * Small adapter to preserve old bean name (RateLimiterService) expected by tests.
 * Delegates to the new RateLimitService implementation.
 */
@Service
public class RateLimiterService {
    private final RateLimitService delegate;

    public RateLimiterService(RateLimitService delegate) {
        this.delegate = delegate;
    }

    public boolean isAllowed(String clientId) {
        return delegate.isAllowed(clientId);
    }
}
