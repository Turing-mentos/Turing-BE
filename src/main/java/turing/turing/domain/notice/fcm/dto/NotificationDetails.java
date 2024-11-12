package turing.turing.domain.notice.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.member.Role;

@Builder
@AllArgsConstructor
@Getter
public class NotificationDetails {
    Long senderId;
    Role senderRole;
    Long receiverId;
    Role receiverRole;
}
