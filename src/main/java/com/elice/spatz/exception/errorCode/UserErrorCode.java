package com.elice.spatz.exception.errorCode;

import com.elice.spatz.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    EMAIL_ALREADY_IN_USE(HttpStatus.BAD_REQUEST, "이미 사용 중인 사용자 이메일입니다.", "U001"),
    EMAIL_RESIGN_IN_USE(HttpStatus.BAD_REQUEST, "탈퇴한 사용자 이메일입니다.", "U002"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", "U003"),
    EMAIL_VERIFICATION_NOT_COMPLETE(HttpStatus.UNAUTHORIZED, "이메일 인증이 완료되지 않았습니다.", "U004"),
    NICKNAME_ALREADY_IN_USE(HttpStatus.CONFLICT, "이미 사용 중인 사용자 닉네임입니다.", "U005");


    private final HttpStatus status;
    private final String message;
    private final String code;

    @Override
    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
