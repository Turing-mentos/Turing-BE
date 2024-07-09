package turing.turing.domain.studyTime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyTime.StudyTime;

import java.time.LocalTime;

public record StudyTimeReqDto(
        Integer day,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime
) {
        public StudyTime toEntity(StudyRoom studyRoom){
                return new StudyTime(day, startTime, endTime, studyRoom);
        }
}
