package turing.turing.domain.homework.dto;

import lombok.Getter;

@Getter
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
