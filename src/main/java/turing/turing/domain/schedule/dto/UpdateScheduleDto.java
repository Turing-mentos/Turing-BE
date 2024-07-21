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

    public static UpdateScheduleDto of(LocalDate date, LocalTime startTime, LocalTime endTime,
            Integer session) {

        return UpdateScheduleDto.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .session(session)
                .build();
    }
}
