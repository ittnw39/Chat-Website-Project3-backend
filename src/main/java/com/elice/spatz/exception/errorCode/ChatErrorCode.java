package com.elice.spatz.exception.errorCode;

import com.elice.spatz.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;




@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {
    CHANNEL_NOT_FOUND("CHAT_001", "채널을 찾을 수 없습니다."),
    MESSAGE_NOT_FOUND("CHAT_002", "메시지를 찾을 수 없습니다."),
    INVALID_MESSAGE_CONTENT("CHAT_003", "유효하지 않은 메시지 내용입니다."),
    USER_NOT_AUTHORIZED("CHAT_004", "사용자가 권한이 없습니다.");


    private final String code;
    private final String message;

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        // WebSocket에서는 HTTP 상태 코드를 사용 X  ->  null을 반환
        return null;
    }



}
