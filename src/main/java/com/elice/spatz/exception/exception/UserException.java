package com.elice.spatz.exception.exception;


import com.elice.spatz.exception.errorCode.UserErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserException extends RuntimeException {
    private final UserErrorCode userErrorCode;
}
