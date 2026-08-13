package com.example.bankapi.config;

import com.example.bankapi.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.contains("/api/v1/transfers")) {

            String clientIp = request.getRemoteAddr()+"TRANSFER";
            long remaining = rateLimitService.tryConsume(clientIp, 1);
            if (remaining >= 0) {
                response.addHeader("X-Rate-Limit-Remaining", String.valueOf(remaining));
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(429); // too many requests
                response.getWriter().write("Too many requests");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
