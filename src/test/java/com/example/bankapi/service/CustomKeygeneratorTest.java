package com.example.bankapi.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CustomKeygeneratorTest {

    @Test
    void generate_buildsKeyWithParams() throws NoSuchMethodException {
        CustomKeygenerator gen = new CustomKeygenerator();

        class Target {
            public void foo(String s) {}
        }

        Method m = Target.class.getMethod("foo", String.class);
        Target t = new Target();

        Object key = gen.generate(t, m, "param1");

        assertNotNull(key);
        String ks = key.toString();
        assertTrue(ks.contains("Target.foo"));
        assertTrue(ks.contains("param1"));
    }
}
