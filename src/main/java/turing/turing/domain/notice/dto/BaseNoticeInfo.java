package turing.turing.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.Role;

@Getter
@Builder
public class BaseNoticeInfo {

    public Long senderId;
    public Role senderRole;
    public Long receiverId;
    public Role receiverRole;
}
