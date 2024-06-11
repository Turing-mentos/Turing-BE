package turing.turing.domain.homework.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import turing.turing.domain.homework.Homework;
import turing.turing.domain.notebook.Notebook;

@Getter
@NoArgsConstructor
public class DetailedHomeworkDto {
    private Long homeworkId;

    private String category;

    private String title;

    private String rangeType;

    private Integer rangeStart;

    private Integer rangeEnd;

    private String content;

    private String memo;

    private Long notebookId;

    @Builder
    public DetailedHomeworkDto(Long homeworkId, String category, String title, String rangeType, Integer rangeStart,
            Integer rangeEnd, String content, String memo, Long notebookId) {
        this.homeworkId = homeworkId;
        this.category = category;
        this.title = title;
        this.rangeType = rangeType;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.content = content;
        this.memo = memo;
        this.notebookId = notebookId;
    }

    public Homework toEntity(Notebook notebook) {
        return Homework.builder()
                .category(category)
                .title(title)
                .rangeType(rangeType)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .content(content)
                .memo(memo)
                .notebook(notebook)
                .build();
    }
}
