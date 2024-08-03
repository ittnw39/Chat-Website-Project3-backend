package com.elice.spatz.domain.chat.controller;

import com.elice.spatz.domain.chat.entity.ChatMessage;
import com.elice.spatz.domain.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;


    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }
    /**
     * 채팅방에 들어올때
     * 추후 로그인한 사용자 정보 가져와야함
     */
    @MessageMapping("/chat/enter")
    public void enterChatChannel(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        // 웹소켓 세션에 username, senderId, channelId 저장
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        // userService에서 username을 받아오는걸로 수정
        headerAccessor.getSessionAttributes().put("senderId", chatMessage.getSenderId());
        headerAccessor.getSessionAttributes().put("channelId", chatMessage.getChannelId());

        //username을 받아와서 입장시 메세지 수정
        chatMessage.setContent(chatMessage.getSenderId() + "님이 입장하셨습니다.");
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getChannelId(), chatMessage);

    }

    /**
     * 클라이언트에서 받아온 메세지를 db에 저장후 브로드캐스트
     */
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMessage = chatService.sendAndSaveMessage(chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getChannelId(), savedMessage);
    }


    /**
     * 클라이언트에서 받아온 메세지 수정후 db에 저장 -> 브로드캐스트
     */
    @MessageMapping("/chat/edit")
    public void updateMessage(@Payload ChatMessage chatMessage) {
        ChatMessage updatedMessage = chatService.updatedMessage(
                chatMessage.getChannelId(),
                chatMessage.getId(),
                chatMessage.getContent()
        );

        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getChannelId() + "/update", updatedMessage);
    }

    /**
     * 클라이언트에서 삭제요청 db에서 삭제 후 -> 브로드캐스트
     */
    @MessageMapping("/chat/delete")
    public void deleteMessage(@Payload ChatMessage chatMessage) {
        Long result = chatService.deleteMessage(chatMessage.getChannelId(), chatMessage.getId());

        if(result > 0) {
            messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getChannelId() + "/delete", chatMessage);
        }


    }

}


