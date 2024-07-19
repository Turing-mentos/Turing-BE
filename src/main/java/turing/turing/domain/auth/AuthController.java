package turing.turing.domain.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import turing.turing.domain.auth.apple.VerifyAppleRequest;
import turing.turing.domain.auth.dto.LoginRequest;
import turing.turing.domain.auth.dto.LoginResponse;
import turing.turing.domain.auth.dto.TokenReIssueRequest;
import turing.turing.domain.auth.jwt.TokenResponse;
import turing.turing.domain.auth.kakao.VerifyKakaoRequest;
import turing.turing.domain.member.Provider;

@Tag(name = "Auth", description = "인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/verify/apple")
    public ResponseEntity<TokenResponse> verifyAppleEmail(@RequestBody @Valid VerifyAppleRequest request) {
        String email = authService.verifyWithApple(request);

        return ResponseEntity.ok(authService.confirmAssign(email, Provider.APPLE));
    }

    @PostMapping("/verify/kakao")
    public ResponseEntity<TokenResponse> verifyKakaoEmail(@RequestBody @Valid VerifyKakaoRequest request) {
        String email = request.getEmail();

        return ResponseEntity.ok(authService.confirmAssign(email, Provider.KAKAO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody @Valid TokenReIssueRequest request) {
        TokenResponse response = authService.reissue(request);

        return ResponseEntity.ok(response);
    }
}
