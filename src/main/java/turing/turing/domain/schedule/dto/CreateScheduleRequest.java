package turing.turing.domain.schedule.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import turing.turing.domain.studyTime.dto.StudyTimeReqDto;

@Getter
@AllArgsConstructor
public class CreateScheduleRequest {

    private Long studyRoomId;
    private String studentName;
    private String subject;

    private List<StudyTimeReqDto> studyTimeList;
    private Integer baseSession;
    private LocalDate startDate;
}
