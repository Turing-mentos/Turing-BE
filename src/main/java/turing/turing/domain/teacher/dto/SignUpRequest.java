package turing.turing.domain.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;

@Getter
@AllArgsConstructor
public class SignUpRequest {

    private String email;
    private Role role;
    private String name;
    private Provider provider;
}
