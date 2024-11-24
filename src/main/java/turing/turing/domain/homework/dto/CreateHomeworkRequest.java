package turing.turing.domain.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateHomeworkRequest {

    private String category;

    private String title;

    private String rangeType;

    private Integer rangeStart;

    private Integer rangeEnd;

    private String content;

    private String memo;

    private Long notebookId;
}
