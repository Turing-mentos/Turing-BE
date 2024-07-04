package turing.turing.domain.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.Role;
import turing.turing.domain.auth.jwt.JwtTokenProvider;
import turing.turing.domain.teacher.dto.SignUpResponse;
import turing.turing.domain.teacher.dto.TeacherSignUpRequest;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignUpResponse signUp(TeacherSignUpRequest request) {
        String email = request.getEmail();

        if (teacherRepository.findByEmail(email).isPresent()) {
            throw new RestApiException(CommonErrorCode.BAD_REQUEST);
        }

        Teacher teacher = new Teacher(email, request.getName(), request.getProvider());
        Teacher savedTeacher = teacherRepository.save(teacher);

        String accessToken = jwtTokenProvider.createAccessToken(email, Role.TEACHER);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        return SignUpResponse.builder()
                .id(savedTeacher.getId())
                .role(Role.TEACHER)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
