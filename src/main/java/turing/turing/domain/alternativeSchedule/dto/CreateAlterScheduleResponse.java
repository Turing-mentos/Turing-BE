package turing.turing.domain.alternativeSchedule.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import turing.turing.domain.notice.dto.response.BaseNoticeInfo;

@Getter
@SuperBuilder
public class CreateAlterScheduleResponse extends BaseNoticeInfo {

    private Long firstAlterScheduleId;
    private LocalDate scheduleDate;
}
