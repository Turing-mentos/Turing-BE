package turing.turing.domain.schedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyScheduleRequest {

    private Long scheduleId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

}
