package turing.turing.domain.auth.apple;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class VerifyAppleRequest {

    @NotEmpty
    private String appleIdToken;
}
