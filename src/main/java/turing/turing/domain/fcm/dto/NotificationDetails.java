package turing.turing.domain.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class NotificationDetails {
    Long senderId;
    String senderRole;
    Long receiverId;
    String receiverRole;
}
