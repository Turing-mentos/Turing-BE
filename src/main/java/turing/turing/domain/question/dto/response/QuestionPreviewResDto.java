package turing.turing.domain.question.dto.response;

import turing.turing.domain.question.Question;

import java.sql.Timestamp;

public record QuestionPreviewResDto(
        Long id,
        String category,
        String title,
        String content,
        Boolean solveStatus,
        Boolean pinStatus,
        Timestamp createdAt
) {
    public static QuestionPreviewResDto of(Question question){
        return new QuestionPreviewResDto(
                question.getId(),
                question.getCategory(),
                question.getTitle(),
                question.getContent(),
                question.getSolveStatus(),
                question.getPinStatus(),
                question.getCreatedAt()
        );
    }
}
