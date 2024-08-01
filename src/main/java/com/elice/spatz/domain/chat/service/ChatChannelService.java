package com.elice.spatz.domain.chat.service;

import com.elice.spatz.domain.chat.entity.ChatChannel;
import com.elice.spatz.domain.chat.repository.ChatChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ChatChannelService {

    private final ChatChannelRepository chatChannelRepository;

    @Autowired
    public ChatChannelService(ChatChannelRepository chatChannelRepository) {
        this.chatChannelRepository = chatChannelRepository;
    }

    // 채널 조회
    public ChatChannel getChannelById(Long ChannelId) {
        return chatChannelRepository.findById(ChannelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));
    }

    // 채널 생성
    public void createChannel(ChatChannel chatChannel) {
        chatChannelRepository.save(chatChannel);
    }

    // 채널 이름 수정
    public ChatChannel updateChannelName(Long channelId, String name) {
        ChatChannel chatChannel = chatChannelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        chatChannel.setName(name);
        return chatChannelRepository.save(chatChannel);
    }

    // 채널 삭제
    public void deleteChannel(Long channelId) {
        if (!chatChannelRepository.existsById(channelId)) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다.");
        }
        chatChannelRepository.deleteById(channelId);    }


}
