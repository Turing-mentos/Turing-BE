package turing.turing.domain.exam.dto;

import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.member.Role;

@Getter
@Builder
public class CreateExamResponse {

    private Long examId;
    public Long senderId;
    public Role senderRole;
    public Long receiverId;
    public Role receiverRole;
}
