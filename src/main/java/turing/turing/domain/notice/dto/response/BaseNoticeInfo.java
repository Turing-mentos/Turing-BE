package turing.turing.domain.notice.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import turing.turing.domain.member.Role;

@Getter
@SuperBuilder
public class BaseNoticeInfo {

    protected Long senderId;
    protected Role senderRole;
    protected Long receiverId;
    protected Role receiverRole;

    public void setSender(Long senderId, Role senderRole) {
        this.senderId = senderId;
        this.senderRole = senderRole;
    }
}
