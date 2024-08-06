package com.elice.spatz.exception.errorCode;

import com.elice.spatz.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerErrorCode implements ErrorCode {
    SERVER_NOT_FOUND(HttpStatus.NOT_FOUND,"서버를 찾을 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
