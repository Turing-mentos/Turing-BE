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

    //프로필
    UPDATE_NOT_ALLOWED(HttpStatus.BAD_GATEWAY, "프로필은 일년에 한 번만 변경할 수 있습니다.");
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
