package com.elice.spatz.exception.exception;


import com.elice.spatz.exception.errorCode.ServerErrorCode;
import com.elice.spatz.exception.errorCode.UserErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerException extends RuntimeException {
    private final ServerErrorCode serverErrorCode;
}
