package turing.turing.domain.teacher.dto;

import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.Role;

@Getter
@Builder
public class SignUpResponse {

    private Long id;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
