package turing.turing.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.member.Role;

@Getter
@Builder
public class SignUpResponse {

    private Long id;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
