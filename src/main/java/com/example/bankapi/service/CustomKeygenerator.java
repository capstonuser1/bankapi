package com.example.bankapi.service;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component("customGenerator")
public class CustomKeygenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, java.lang.reflect.Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(target.getClass().getSimpleName()).append(".").append(method.getName());
        for (Object param : params) {
            key.append(".").append(param.toString());
        }
        return key.toString();
    }

}
