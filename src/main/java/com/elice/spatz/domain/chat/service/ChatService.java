package com.elice.spatz.domain.chat.service;

import com.elice.spatz.domain.chat.entity.ChatMessage;
import com.elice.spatz.exception.errorCode.ChatErrorCode;
import com.elice.spatz.exception.exception.ChatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class ChatService {

    // 메시지의 유효 기간을 2일(초 단위)로 설정
    private static final long MESSAGE_TTL = 2 * 24 * 60 * 60;

    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public ChatService(RedisTemplate<String, ChatMessage> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 메세지 저장
     */
    public ChatMessage SaveMessage(ChatMessage chatMessage) {

        if (chatMessage == null || chatMessage.getChannelId() == null || chatMessage.getContent() == null) {
            throw new ChatException(ChatErrorCode.INVALID_MESSAGE_CONTENT);
        }
        // 새로운 메시지 ID 생성
        String messageId = createNewMessageId(chatMessage.getChannelId());
        chatMessage.setId(messageId);
        chatMessage.setCreatedTime(LocalDateTime.now());

        String messageKey = "message:" + chatMessage.getChannelId() + ":" + messageId;
        String channelKey = "channel:" + chatMessage.getChannelId();


        redisTemplate.opsForValue().set(messageKey, chatMessage, MESSAGE_TTL, TimeUnit.SECONDS);
        redisTemplate.opsForList().rightPush(channelKey, chatMessage);
        redisTemplate.expire(channelKey, MESSAGE_TTL, TimeUnit.SECONDS);


        return chatMessage;
    }

    /**
     * 특정 채널의 모든 메시지를 조회
     *
     * @param channelId 조회할 채널의 ID
     * @return 해당 채널의 모든 메시지 리스트
     */
    public List<ChatMessage> getAllMessagesInChannel(String channelId) {

        if (channelId == null) {
            throw new ChatException(ChatErrorCode.CHANNEL_NOT_FOUND);
        }
        String channelKey = "channel:" + channelId;
        List<ChatMessage> messages = redisTemplate.opsForList().range(channelKey, 0, -1);

        if (messages == null || messages.isEmpty()) {
            throw new ChatException(ChatErrorCode.MESSAGE_NOT_FOUND);
        }

        return messages;
    }

    /**
     * 특정 사용자가 특정 채널에서 보낸 메시지를 조회
     *
     * @param channelId 조회할 채널의 ID
     * @param senderId  조회할 사용자의 ID
     * @return 해당 사용자가 해당 채널에서 보낸 모든 메시지 리스트
     */
    public List<ChatMessage> getMessagesBySender(String channelId, String senderId) {
        List<ChatMessage> allMessages = getAllMessagesInChannel(channelId);
        return allMessages.stream()
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    /**
     * 특정 메시지를 수정
     *
     * @param channelId  메시지가 속한 채널의 ID
     * @param messageId  수정할 메시지의 ID
     * @param newContent 새로운 메시지 내용
     * @return 수정된 메시지 객체. 메시지가 존재하지 않으면 null 반환
     */
    public ChatMessage updateMessage(String channelId, String messageId, String newContent) {

        if (channelId == null || messageId == null || newContent == null || newContent.trim().isEmpty()) {
            throw new ChatException(ChatErrorCode.INVALID_MESSAGE_CONTENT);
        }

        String channelKey = "channel:" + channelId;
        List<ChatMessage> messages = redisTemplate.opsForList().range(channelKey, 0, -1);

        for (int i = 0; i < messages.size(); i++) {
            ChatMessage message = messages.get(i);
            if (message.getId().equals(messageId)) {
                message.updateContent(newContent);
                redisTemplate.opsForList().set(channelKey, i, message);
                return message;
            }
        }
        // 메세지를 찾지 못한 경우 예외발생
        throw new ChatException(ChatErrorCode.MESSAGE_NOT_FOUND);

    }

    /**
     * 특정 메시지를 삭제
     *
     * @param channelId 메시지가 속한 채널의 ID
     * @param messageId 삭제할 메시지의 ID
     * @return 삭제된 메시지 객체. 메시지가 존재하지 않으면 null 반환
     */
    public ChatMessage deleteMessage(String channelId, String messageId) {
        String channelKey = "channel:" + channelId;
        String messageKey = "message:" + channelId + ":" + messageId;

        // 개별 메시지 삭제
        ChatMessage deletedMessage = redisTemplate.opsForValue().get(messageKey);

        if (deletedMessage == null) {
            throw new ChatException(ChatErrorCode.MESSAGE_NOT_FOUND);
        }

        // 채널 리스트에서 메시지 삭제
        redisTemplate.delete(messageKey);
        redisTemplate.opsForList().remove(channelKey, 1, deletedMessage);

        return deletedMessage;
    }

    /**
     * 특정 채널의 최근 메시지 50개 조회
     *
     * @param channelId 조회할 채널의 ID
     * @return 최근 메시지 50개 리스트
     */
    public List<ChatMessage> getRecentMessages(String channelId) {

        if (channelId == null) {
            throw new ChatException(ChatErrorCode.CHANNEL_NOT_FOUND);
        }

        String channelKey = "channel:" + channelId;
        long size = redisTemplate.opsForList().size(channelKey);
        // 메시지가 없는 경우 빈 리스트를 반환
        if (size == 0) {
            return new ArrayList<>();
        }

        return redisTemplate.opsForList().range(channelKey, Math.max(0, size - 50), -1);
    }

    // 새 메시지 ID 생성 메서드
    private String createNewMessageId(String channelId) {
        String fiveUuid = UUID.randomUUID().toString().substring(0, 5);
        String fourChannelId = String.format("%04d", Integer.parseInt(channelId) % 10000);
        return fourChannelId + fiveUuid;
    }

}