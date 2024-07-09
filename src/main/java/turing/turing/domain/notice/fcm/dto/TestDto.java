package turing.turing.domain.notice.fcm.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class TestDto {
    Long senderId;
    String senderRole;
    Long receiverId;

    String receiverRole;

    Long questionId;

    String category;

    LocalDate scheduleDate;

    LocalDate alternativeDate;
}
