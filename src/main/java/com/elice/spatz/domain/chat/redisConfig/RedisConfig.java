package com.elice.spatz.domain.chat.redisConfig;

import com.elice.spatz.domain.chat.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, Integer.parseInt(port));
    }


    // RedisTemplate 설정
    @Bean
    public RedisTemplate<String, ChatMessage> redisTemplate() {
        RedisTemplate<String, ChatMessage> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // key 직렬화
        template.setKeySerializer(new StringRedisSerializer());

        // value 직렬화 (ChatMessage 객체를 JSON으로 직렬화)
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));

        return template;
    }
}

