package com.elice.spatz.domain.chat.mapper;

import com.elice.spatz.domain.chat.dto.ChatChannelDTO;
import com.elice.spatz.domain.chat.entity.ChatChannel;

public class ChatChannelMapper {
    public static ChatChannelDTO toDTO(ChatChannel chatChannel) {
        return new ChatChannelDTO(
                chatChannel.getId(),
                chatChannel.getName(),
                chatChannel.getServerId()
        );
    }

    public static ChatChannel toEntity(ChatChannelDTO chatChannelDTO) {
        return new ChatChannel(
                chatChannelDTO.getId(),
                chatChannelDTO.getName(),
                chatChannelDTO.getServerId()
        );
    }
}
