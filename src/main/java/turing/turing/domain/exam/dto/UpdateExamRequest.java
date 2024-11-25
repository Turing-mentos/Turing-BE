package turing.turing.domain.exam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateExamRequest {

    @NotNull(message = "시험Id는 필수입니다.")
    @Positive(message = "시험Id는 양수여야 합니다.")
    private Long examId;

    @NotBlank(message = "시험명은 필수입니다.")
    private String examName;

    @NotNull(message = "시작일은 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수입니다.")
    private LocalDate endDate;

    @NotNull(message = "과외공간Id는 필수입니다.")
    @Positive(message = "과외공간Id는 양수여야 합니다.")
    private Long studyRoomId;

    @Builder
    private UpdateExamRequest(Long examId, String examName, LocalDate startDate, LocalDate endDate,
            Long studyRoomId) {
        this.examId = examId;
        this.examName = examName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studyRoomId = studyRoomId;
    }
}
