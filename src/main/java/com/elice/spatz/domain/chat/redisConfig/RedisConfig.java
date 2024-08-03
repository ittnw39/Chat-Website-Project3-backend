package com.elice.spatz.domain.chat.redisConfig;

import com.elice.spatz.domain.chat.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
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
        @Bean
        public RedisTemplate<String, ChatMessage> redisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, ChatMessage> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);

            // Redis key <--> String  직렬화/역직렬화
            template.setKeySerializer(new StringRedisSerializer());

            // ChatMessage <--> JSON  직렬화/역직렬화합니다.
            Jackson2JsonRedisSerializer<ChatMessage> serializer = new Jackson2JsonRedisSerializer<>(ChatMessage.class);
            template.setValueSerializer(serializer);

            // Hash 작업을 위한 key와 value의 직렬화/역직렬화 설정
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(serializer);

            return template;
        }

        // Redis Hash 이용 --> HashOperations 빈 생성
        @Bean
        public HashOperations<String, String, ChatMessage> hashOperations(RedisTemplate<String, ChatMessage> redisTemplate) {
            return redisTemplate.opsForHash();
        }


}
