package turing.turing.domain.alternativeSchedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlterScheduleDto {

    private Long alterScheduleId;
    private LocalDate date;
//    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
}
