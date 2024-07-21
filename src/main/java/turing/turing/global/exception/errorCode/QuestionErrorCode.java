package turing.turing.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuestionErrorCode implements ErrorCode {

    // Question
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Question Not Found"),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment Not Found")
    ;



    private final HttpStatus httpStatus;
    private final String message;
}