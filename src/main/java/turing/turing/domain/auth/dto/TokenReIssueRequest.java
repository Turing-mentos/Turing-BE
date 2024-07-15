package turing.turing.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenReIssueRequest {

    private String refreshToken;
}
