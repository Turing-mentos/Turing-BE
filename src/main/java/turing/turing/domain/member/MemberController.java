package turing.turing.domain.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.member.dto.SignUpRequest;
import turing.turing.domain.member.dto.SignUpResponse;

@Tag(name = "Member", description = "회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest request) {

        SignUpResponse response = memberService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "회원탈퇴")
    @DeleteMapping("")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails user) {

        memberService.deleteMember(user);

        return ResponseEntity.noContent().build();
    }
}
