package turing.turing.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND, "Teacher Not Found"),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Student Not Found"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "User Already Exists"),
    INACTIVE_USER(HttpStatus.FORBIDDEN, "User Inactive"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
