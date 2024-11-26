package turing.turing.domain.homework.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.global.util.validation.range.ValidRange;

@Getter
@NoArgsConstructor
@ValidRange(startField = "rangeStart", endField = "rangeEnd", type = Integer.class, message = "끝범위는 시작범위와 같거나 커야 합니다.")
public class CreateHomeworkRequest {

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "범위타입은 필수입니다.")
    private String rangeType;

    @NotNull(message = "시작범위는 필수입니다.")
    @Positive(message = "시작범위는 양수여야 합니다.")
    private Integer rangeStart;

    @NotNull(message = "끝범위는 필수입니다.")
    @Positive(message = "끝범위는 양수여야 합니다.")
    private Integer rangeEnd;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private String memo;

    @NotNull(message = "알림장Id는 필수입니다.")
    @Positive(message = "알림장Id는 양수여야 합니다.")
    private Long notebookId;

    @Builder
    private CreateHomeworkRequest(String category, String title, String rangeType,
            Integer rangeStart,
            Integer rangeEnd, String content, String memo, Long notebookId) {
        this.category = category;
        this.title = title;
        this.rangeType = rangeType;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.content = content;
        this.memo = memo;
        this.notebookId = notebookId;
    }
}
