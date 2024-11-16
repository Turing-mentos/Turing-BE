package turing.turing.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExamErrorCode implements ErrorCode {

    EXAM_NOT_FOUND(HttpStatus.NOT_FOUND, "Exam Not Found");

    private final HttpStatus httpStatus;
    private final String message;
}
