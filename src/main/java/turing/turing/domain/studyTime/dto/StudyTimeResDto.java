package turing.turing.domain.studyTime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import turing.turing.domain.studyTime.StudyTime;

import java.time.LocalTime;

public record StudyTimeResDto(
        Integer day,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime
) {
        public static StudyTimeResDto of(StudyTime studyTime){
                return new StudyTimeResDto(studyTime.getDay(), studyTime.getStartTime(), studyTime.getEndTime());
        }
}
