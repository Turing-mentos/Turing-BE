package turing.turing.domain.question.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import turing.turing.domain.question.Question;

import java.sql.Timestamp;
import java.time.LocalDate;

public record QuestionPreviewResDto(
        Long id,
        String category,
        String title,
        String content,
        Boolean solveStatus,
        Boolean pinStatus,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Integer commentCount
) {
    public static QuestionPreviewResDto of(Question question){
        return new QuestionPreviewResDto(
                question.getId(),
                question.getCategory(),
                question.getTitle(),
                question.getContent(),
                question.getSolveStatus(),
                question.getPinStatus(),
                question.getCreatedAt().toLocalDateTime().toLocalDate(),
                question.getCommentCount()
        );
    }
}
