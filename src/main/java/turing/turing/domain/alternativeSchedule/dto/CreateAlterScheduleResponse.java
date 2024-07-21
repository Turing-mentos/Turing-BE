package turing.turing.domain.alternativeSchedule.dto;

import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.member.Role;

@Getter
@Builder
public class CreateAlterScheduleResponse {

    private Long firstAlterScheduleId;
    public Long senderId;
    public Role senderRole;
    public Long receiverId;
    public Role receiverRole;
}
