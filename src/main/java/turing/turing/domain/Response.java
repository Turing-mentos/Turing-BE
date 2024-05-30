package turing.turing.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Response<T> {

    private final HttpStatus httpStatus;
    private final String message;
    private T data;

    public static <T> Response<T> ofSuccess(String message, T data) {
        return new Response<>(HttpStatus.OK, message, data);
    }

    public static <T> Response<T> ofCreated(String message, T data) {
        return new Response<>(HttpStatus.CREATED, message, data);
    }
}
