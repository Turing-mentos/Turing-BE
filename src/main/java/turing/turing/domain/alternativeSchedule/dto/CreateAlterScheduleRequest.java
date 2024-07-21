package turing.turing.domain.alternativeSchedule.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAlterScheduleRequest {
    private Long targetScheduleId;
    private Map<LocalDate, List<TimePairDto>> alterScheduleList;
}
