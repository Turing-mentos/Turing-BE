package turing.turing.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;

@Getter
@Builder
public class LoginResponse {

    private Role role;
    private Long memberId;
    private String firstName;
    private String lastName;
    private String university;
    private String department;
    private String studentNumber;
    private Provider provider;

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}