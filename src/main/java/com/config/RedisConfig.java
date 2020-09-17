package com.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    @Bean
    public RedisTemplate<String , Serializable> redisCacheTemplate (LettuceConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        // key的序列化器设置成StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        // 解决中文乱码问题
        template.setValueSerializer(new GenericToStringSerializer<>(String.class, StandardCharsets.UTF_8));
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
