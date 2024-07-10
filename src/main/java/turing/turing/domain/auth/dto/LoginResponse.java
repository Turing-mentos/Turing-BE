package turing.turing.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import turing.turing.domain.Role;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Role role;
    private Long id;
}
