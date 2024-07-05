package turing.turing.domain.notice.fcm.dto;

import lombok.Builder;

@Builder
public class TestDto {
    Long senderId;
    String senderRole;
    Long receiverId;

    String receiverRole;

    Long commentId;

}
