package turing.turing.domain.schedule.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import turing.turing.domain.notice.dto.BaseNoticeInfo;

@Getter
@SuperBuilder
public class ModifyScheduleResponse extends BaseNoticeInfo {

    private Long scheduleId;
    private LocalDate prevDate;
    private LocalDate alterDate;
}
