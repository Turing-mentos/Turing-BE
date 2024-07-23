package turing.turing.domain.schedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleDto {
    private Long scheduleId;

    private LocalDate date;

    private String studentName;

    private String subject;

    private Integer session;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private Long studyRoomId;

    private Integer BaseSession;
}
