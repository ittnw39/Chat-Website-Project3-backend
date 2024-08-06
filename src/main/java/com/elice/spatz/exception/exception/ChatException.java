package com.elice.spatz.exception.exception;

import com.elice.spatz.exception.errorCode.ChatErrorCode;
import lombok.Getter;

@Getter
public class ChatException extends RuntimeException {
    private final ChatErrorCode chatErrorCode;

    public ChatException(ChatErrorCode chatErrorCode) {
        super(chatErrorCode.getMessage());
        this.chatErrorCode = chatErrorCode;
    }
}