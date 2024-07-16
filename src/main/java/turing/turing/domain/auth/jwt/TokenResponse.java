package turing.turing.domain.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {

    private String email;
    private String accessToken;
    private String refreshToken;
}
