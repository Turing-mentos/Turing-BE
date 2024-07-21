package turing.turing.domain.auth.kakao;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class VerifyKakaoRequest {

    @NotEmpty
    private String email;
}
