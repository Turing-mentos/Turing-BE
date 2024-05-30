package turing.turing.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import turing.turing.global.exception.errorCode.ErrorCode;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {

    private final ErrorCode errorCode;
}
