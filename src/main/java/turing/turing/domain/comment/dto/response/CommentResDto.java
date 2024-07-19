package turing.turing.domain.comment.dto.response;

import turing.turing.domain.comment.Comment;

public record CommentResDto(
        Long id,
        String imageUrl,
        String content
) {
    public static CommentResDto of(Comment comment) {
        return new CommentResDto(
                comment.getId(),
                comment.getImageUrl(),
                comment.getContent()
        );
    }
}
