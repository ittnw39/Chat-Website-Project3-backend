package com.elice.spatz.domain.chat.service;

import com.elice.spatz.domain.chat.entity.ChatMessage;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatService {

    private final HashOperations<String, String, ChatMessage> hashOperations;
    private final RedisTemplate<String, ChatMessage> redisTemplate;

    private final SimpMessagingTemplate messagingTemplate;


    public ChatService(RedisTemplate<String, ChatMessage> redisTemplate, SimpMessagingTemplate messagingTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.messagingTemplate = messagingTemplate;
    }

    // 채팅 메세지 저장(Redis)
    public ChatMessage sendAndSaveMessage(ChatMessage chatMessage) {
        String messageId = createNewMessageId(chatMessage.getChannelId());
        chatMessage.setId(messageId);
        chatMessage.setCreatedTime(LocalDateTime.now());

        hashOperations.put(chatMessage.getChannelId(), messageId, chatMessage);
        return chatMessage;
    }

     // 채팅방의 모든 메세지 조회
    public List<ChatMessage> getAllMessages(String channelId) {
        // channelId 에 대한 collection으로 반환되는 값들을 List로 반환
        return new ArrayList<>(hashOperations.values(channelId));


    }

    // 특정 사용자의 메세지 조회
    public List<ChatMessage> getMessagesByUserId(String channelId, String userId) {
        Map<String, ChatMessage> messagesMap = hashOperations.entries(channelId);

        List<ChatMessage> userMessages = new ArrayList<>();
        for (ChatMessage message : messagesMap.values()) {
            //  senderId와 userId가 같은 경우 해당하는 메세지 추가
            if (message.getSenderId().equals(userId)) {
                userMessages.add(message);
            }
        }

        return userMessages;

    }


    // 최근 메세지 50개 조회
    public List<ChatMessage> getRecentMessages(String channelId) {
        // getAllMessages 메서드를 이용 -> 모든 메세지 조회
        List<ChatMessage> allMessages = getAllMessages(channelId);

        List<ChatMessage> recentMessages = new ArrayList<>();

        // 생성 시간 기준으로 정렬
        // 최신 메세지가 리스트 앞에 위치
        Collections.sort(allMessages, new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage o1, ChatMessage o2) {
                return o2.getCreatedTime().compareTo(o1.getCreatedTime());
            }
        });

        int messageCount = Math.min(allMessages.size(), 50);
        for (int i = 0; i < messageCount; i++) {
            recentMessages.add(allMessages.get(i));
        }

        return recentMessages;


    }


    // 특정 메세지 수정
    // 예외 처리 필요
    public ChatMessage updatedMessage(String channelId, String messageId, String newContent) {
        ChatMessage updatedMessage = hashOperations.get(channelId, messageId);
        updatedMessage.updateContent(newContent);
        hashOperations.put(channelId, messageId, updatedMessage);

        return updatedMessage;
    }

    // 특정 메세지 삭제
    public Long deleteMessage(String channelId, String messageId) {
        //성공하면 1, 실패하면 0 반환
        // 예외처리 해줘야함
        return hashOperations.delete(channelId, messageId);
    }


    // 메세지 아이디 생성 -> 채팅채널 ID + UUID
    private String createNewMessageId(String channelId) {
        String fiveUuid = UUID.randomUUID().toString().substring(0, 5);
        String fourChannelId = String.format("%04d", Integer.parseInt(channelId) % 10000);
        return fourChannelId + fiveUuid;
    }

}
