package turing.turing.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import turing.turing.domain.homework.Homework;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;
import turing.turing.global.exception.errorCode.UserErrorCode;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("")
    public ResponseEntity<Homework> sectest() {
        Homework homework = new Homework();
        return ResponseEntity.ok(homework);
    }

    @GetMapping("/exception/server")
    public Response<String> serverException() {
        throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/exception/param")
    public Response<String> paramException() {
        throw new RestApiException(CommonErrorCode.BAD_REQUEST);
    }

    @GetMapping("/exception/notfound")
    public Response<String> notfoundException() {
        throw new RestApiException(CommonErrorCode.NOT_FOUND);
    }

    @GetMapping("/exception/user")
    public Response<String> userException() {
        throw new RestApiException(UserErrorCode.INACTIVE_USER);
    }
}
