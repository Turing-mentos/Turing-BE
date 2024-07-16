package turing.turing.domain.question.dto.request;

public record QuestionReqDto(
        String title,
        String category,
        String content,
        String importance
) {
}
