package turing.turing.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid Parameter"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource Not Exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "No Content")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
