package turing.turing.domain.exam.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExamRequest {

    private String examName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long studyRoomId;
}
