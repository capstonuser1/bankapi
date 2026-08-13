//package com.example.bankapi.config;
//
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
//@Configuration
//public class RedisConfig {
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//
//        // Set the cache configuration with a TTL of 70 minutes and custom serializers
//
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(70))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(mapper)));
//    }
//}

package com.example.bankapi.config;
import io.lettuce.core.RedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Bean
    public RedisClient redisClient() {
        return RedisClient.create("redis://host.docker.internal:6379");
    }
}