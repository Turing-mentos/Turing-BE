package turing.turing.domain.member.dto;

import lombok.Getter;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;

@Getter
public class SignUpRequest {

    private String email;
    private Role role;
    private String name;
    private Provider provider;
}
