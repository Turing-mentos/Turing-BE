package turing.turing.domain.auth;

import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;
import turing.turing.domain.auth.apple.AppleClient;
import turing.turing.domain.auth.apple.ApplePublicKeyGenerator;
import turing.turing.domain.auth.apple.ApplePublicKeys;
import turing.turing.domain.auth.apple.VerifyAppleRequest;
import turing.turing.domain.auth.apple.AppleTokenParser;
import turing.turing.domain.auth.dto.LoginRequest;
import turing.turing.domain.auth.dto.LoginResponse;
import turing.turing.domain.auth.dto.TokenReIssueRequest;
import turing.turing.domain.auth.jwt.JwtTokenProvider;
import turing.turing.domain.auth.jwt.TokenResponse;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.UserErrorCode;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class AuthService {

    private final AppleTokenParser appleTokenParser;
    private final AppleClient appleClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String verifyWithApple(final VerifyAppleRequest request) {
        String appleIdToken = request.getAppleIdToken();
        Map<String, String> appleTokenHeader = appleTokenParser.parseHeader(appleIdToken);
        ApplePublicKeys applePublicKeys = appleClient.getApplePublicKeys();
        PublicKey publicKey = applePublicKeyGenerator.generate(appleTokenHeader, applePublicKeys);
        Claims claims = appleTokenParser.extractClaims(appleIdToken, publicKey);

        return claims.get("email", String.class);
    }

    public TokenResponse confirmAssign(String email, Provider provider) {
        Optional<Teacher> teacher = teacherRepository.findByEmailAndProvider(email, provider);
        Optional<Student> student = studentRepository.findByEmailAndProvider(email, provider);

        String accessToken = null;
        String refreshToken = null;
        if (teacher.isPresent()) {
            accessToken = jwtTokenProvider.createAccessToken(email, teacher.get().getId(), Role.TEACHER);
            refreshToken = jwtTokenProvider.createRefreshToken(email, teacher.get().getId(), Role.TEACHER);
        } else if (student.isPresent()) {
            accessToken = jwtTokenProvider.createAccessToken(email, student.get().getId(), Role.STUDENT);
            refreshToken = jwtTokenProvider.createRefreshToken(email, student.get().getId(), Role.STUDENT);
        }

        return new TokenResponse(email, accessToken, refreshToken);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        String token = request.getAccessToken();
        String fcmToken = request.getFcmToken();
        if (!jwtTokenProvider.validationToken(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        Role role = jwtTokenProvider.getRoleFromToken(token);

        if (role.equals(Role.TEACHER)) {
            Teacher teacher = teacherRepository.findByEmail(email)
                    .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
            teacher.updateFcmToken(fcmToken);
            return new LoginResponse(role, teacher.getId(), teacher.getFirstName(), teacher.getLastName(), teacher.getUniversity(), teacher.getDepartment(), teacher.getStudentNumber());
        } else {
            Student student = studentRepository.findByEmail(email)
                    .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
            student.updateFcmToken(fcmToken);
            return new LoginResponse(role, student.getId(), student.getFirstName(), student.getLastName(), null, null, null);
        }
    }

    public TokenResponse reissue(TokenReIssueRequest request) {
        String token = request.getRefreshToken();

        if (jwtTokenProvider.validationToken(token) && jwtTokenProvider.validationRefreshToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            Role role = jwtTokenProvider.getRoleFromToken(token);
            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

            String accessToken = jwtTokenProvider.createAccessToken(email, memberId, role);
            String refreshToken = jwtTokenProvider.createRefreshToken(email, memberId, role);

            return new TokenResponse(email, accessToken, refreshToken);
        } else {
            throw new IllegalArgumentException("Expired Refresh token");
        }
    }
}