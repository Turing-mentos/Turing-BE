package turing.turing.domain.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class TokenReIssueRequest {

    @NotEmpty
    private String refreshToken;
}
