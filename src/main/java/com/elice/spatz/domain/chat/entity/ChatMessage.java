package com.elice.spatz.domain.chat.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ChatMessage implements Serializable {
        private String id;
        private String channelId;
        private String senderId;
        private String content;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
        private boolean isDeleted;
        private boolean isEdited;

        public ChatMessage(String channelId, String senderId, String content) {
                this.channelId = channelId;
                this.senderId = senderId;
                this.content = content;
        }

        // 메시지 수정 메서드
        public void updateContent(String newContent) {
                this.content = newContent;
                this.updatedTime = LocalDateTime.now();
                this.isEdited = true;
        }

        // 메시지 삭제 메서드
        public void deleteMessage() {
                this.isDeleted = true;
                this.updatedTime = LocalDateTime.now();
        }

    }
