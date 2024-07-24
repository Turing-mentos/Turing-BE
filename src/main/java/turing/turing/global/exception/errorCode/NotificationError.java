package turing.turing.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationError implements ErrorCode{


    NOTIFICATION_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "No Notification Category"),
    NOTIFICATION_STUDYROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "No studyId IN StudyRoom"),
    NOTIFICATION_FAILURE(HttpStatus.BAD_REQUEST, "NOTIFICATION_FAILURE");
    ;




    private final HttpStatus httpStatus;
    private final String message;
}
