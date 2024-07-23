package turing.turing.domain.schedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateScheduleDto {

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer session;

}
