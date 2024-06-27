package turing.turing.domain.question.dto.response;

import turing.turing.domain.comment.Comment;
import turing.turing.domain.comment.dto.CommentResDto;
import turing.turing.domain.question.Question;

import java.util.List;

public record QuestionWithCommentsResDto(
        Long id,
        String title,
        String category,
        String content,
        String questionImage,
        Boolean solveStatus,
        Boolean pinStatus,
        List<CommentResDto> commentList
) {
    public static QuestionWithCommentsResDto of(Question question, List<Comment> commentList){
        return new QuestionWithCommentsResDto(
                question.getId(),
                question.getTitle(),
                question.getCategory(),
                question.getContent(),
                question.getQuestionImage(),
                question.getSolveStatus(),
                question.getPinStatus(),
                commentList.stream().map(CommentResDto::of).toList()
        );
    }
}
