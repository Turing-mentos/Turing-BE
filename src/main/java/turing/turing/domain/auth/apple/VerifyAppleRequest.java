package turing.turing.domain.auth.apple;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyAppleRequest {

    @NotEmpty
    private String appleIdToken;
}
