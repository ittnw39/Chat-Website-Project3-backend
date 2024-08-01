package com.elice.spatz.domain.chat.repository;

import com.elice.spatz.domain.chat.entity.ChatChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {
    List<ChatChannel> findAllByServerId(String serverId);
}
