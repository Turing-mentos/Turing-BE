package turing.turing.domain.studyRoom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import turing.turing.domain.studyTime.dto.StudyTimeReqDto;

import java.time.LocalDate;
import java.util.List;

public record StudyRoomUpdateReqDto(
        String subject,
        List<StudyTimeReqDto> studyTimes,
        Integer baseSession,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate
) {
}
