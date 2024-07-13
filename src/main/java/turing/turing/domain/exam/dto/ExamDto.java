package turing.turing.domain.exam.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExamDto {

    private Long examId;
    private String examName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long studyRoomId;
}
