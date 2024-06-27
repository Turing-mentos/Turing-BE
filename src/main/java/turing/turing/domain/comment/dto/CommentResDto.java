package turing.turing.domain.comment.dto;

import turing.turing.domain.comment.Comment;

public record CommentResDto(
        Long id,
        String commentImage,
        String content
) {
    public static CommentResDto of(Comment comment) {
        return new CommentResDto(
                comment.getId(),
                comment.getCommentImage(),
                comment.getContent()
        );
    }
}
