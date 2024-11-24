package turing.turing.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotebookErrorCode implements ErrorCode {

    NOTEBOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "Notebook Not Found");

    private final HttpStatus httpStatus;
    private final String message;
}
