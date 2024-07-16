package turing.turing.domain.question.dto.response;

import lombok.Builder;

@Builder
public record QuestionCreateResDto(
        Long questionId,
        // 아래는 알림을 위해 필요한 필드
        Long senderId,
        String senderRole,
        Long receiverId,
        String receiverRole,
        String category
) {
}
