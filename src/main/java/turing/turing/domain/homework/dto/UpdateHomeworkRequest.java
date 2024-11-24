package turing.turing.domain.homework.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateHomeworkRequest {

    private Long homeworkId;

    private String category;

    private String title;

    private String rangeType;

    private Integer rangeStart;

    private Integer rangeEnd;

    private String content;

    private String memo;

    @Builder
    private UpdateHomeworkRequest(Long homeworkId, String category, String title, String rangeType,
            Integer rangeStart, Integer rangeEnd, String content, String memo) {
        this.homeworkId = homeworkId;
        this.category = category;
        this.title = title;
        this.rangeType = rangeType;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.content = content;
        this.memo = memo;
    }
}
