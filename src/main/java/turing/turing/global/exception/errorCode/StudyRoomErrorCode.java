package turing.turing.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StudyRoomErrorCode implements ErrorCode {

    // StudyRoom
    STUDY_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "StudyRoom Not Found"),

    // ConnectionCode
    CONNECTION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "ConnectionCode Not Found"),
    ALREADY_CONNECTED_STUDY_ROOM(HttpStatus.BAD_REQUEST, "Already Connected StudyRoom"),
    CONNECTION_CODE_GENERATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "ConnectionCode Generate Failed (생성 가능한 코드 없음)"),

    // StudyTime
    STUDY_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "StudyTime Not Found"),
    ;




    private final HttpStatus httpStatus;
    private final String message;
}