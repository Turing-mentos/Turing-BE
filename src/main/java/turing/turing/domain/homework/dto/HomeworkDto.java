package turing.turing.domain.homework.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeworkDto {
    private Long homeworkId;
    private String category;
    private String title;
    private String rangeType;
    private Integer rangeStart;
    private Integer rangeEnd;
    private String content;
    private String memo;
    private Boolean isDone;
}