package turing.turing.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentCreateResDto(
        Long commentId,
        // 아래는 알림을 위해 필요한 필드
        Long senderId,
        String senderRole,
        Long receiverId,
        String receiverRole,
        Long questionId
) {
}
