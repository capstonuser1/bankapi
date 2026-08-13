//package com.example.bankapi.interceptor;
//
//import com.example.bankapi.service.RateLimitService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//public class RateLimitInterceptor implements HandlerInterceptor {
//    private final RateLimitService rateLimitService;
//
//    public RateLimitInterceptor(RateLimitService rateLimitService) {
//        this.rateLimitService = rateLimitService;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response, Object handler) throws Exception {
//        String clientIp = request.getRemoteAddr()+"TRANSFER";
//
//        System.out.println("Client IP: " + clientIp);
//
//        long remainingTokens = rateLimitService.tryConsume(clientIp, 1);
//
//        if (remainingTokens == -1) {
//            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//            response.setContentType("application/json");
//            response.getWriter().write("{\"error\": \"Too many requests. Please try again later.\"}");
//            return false;
//        }
//        return true;
//    }
//}
