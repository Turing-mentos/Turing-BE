package turing.turing.domain.exam.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateExamRequest {

    private String examName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long studyRoomId;
}
