package turing.turing.domain.alternativeSchedule.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TimePairDto {
    private LocalTime startTime;
    private LocalTime endTime;
}
