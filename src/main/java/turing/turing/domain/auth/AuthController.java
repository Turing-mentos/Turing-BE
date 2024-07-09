package turing.turing.domain.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import turing.turing.domain.auth.apple.VerifyAppleRequest;
import turing.turing.domain.auth.dto.LoginRequest;
import turing.turing.domain.auth.dto.LoginResponse;
import turing.turing.domain.auth.dto.TokenReIssueRequest;
import turing.turing.domain.auth.jwt.TokenResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/verify/apple")
    public ResponseEntity<TokenResponse> verifyAppleEmail(@RequestBody @Valid VerifyAppleRequest request) {
        String email = authService.verifyWithApple(request);

        return ResponseEntity.ok(authService.confirmAssign(email));
    }

    @GetMapping("/login")
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
