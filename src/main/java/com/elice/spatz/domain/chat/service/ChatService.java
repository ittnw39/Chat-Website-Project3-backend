package com.elice.spatz.domain.chat.service;

import com.elice.spatz.domain.chat.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final HashOperations<String, String, ChatMessage> hashOperations;
    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public ChatService(RedisTemplate<String, ChatMessage> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    // 채팅 메세지 저장(Redis)
    public void saveMessage(ChatMessage chatMessage) {
        hashOperations.put(chatMessage.getChannelId(), chatMessage.getId(), chatMessage);
    }
    // 특정 메세지 조회
    // 특정 메세지 수정
    // 특정 메세지 삭제

}
