package turing.turing.domain.alternativeSchedule.converter;

import turing.turing.domain.alternativeSchedule.AlternativeSchedule;
import turing.turing.domain.alternativeSchedule.dto.AlterScheduleDto;

public class AlterScheduleConverter {

    public static AlterScheduleDto toDto(AlternativeSchedule alternativeSchedule) {
        return AlterScheduleDto.builder()
                .alterScheduleId(alternativeSchedule.getId())
                .date(alternativeSchedule.getScheduleDate())
                .startTime(alternativeSchedule.getStartTime())
                .endTime(alternativeSchedule.getEndTime())
                .build();
    }
}
