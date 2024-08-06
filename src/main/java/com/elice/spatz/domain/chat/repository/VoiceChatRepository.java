package com.elice.spatz.domain.chat.repository;

import com.elice.spatz.domain.chat.entity.VoiceChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceChatRepository extends JpaRepository<VoiceChat, Long> {

}