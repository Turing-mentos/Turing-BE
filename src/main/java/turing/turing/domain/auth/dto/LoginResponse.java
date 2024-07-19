package turing.turing.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import turing.turing.domain.member.Role;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Role role;
    private Long memberId;
    private String name;
    private String university;
    private String department;
    private String studentNumber;
}
