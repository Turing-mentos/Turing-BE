package turing.turing.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.auth.jwt.JwtTokenProvider;
import turing.turing.domain.member.dto.SignUpRequest;
import turing.turing.domain.member.dto.SignUpResponse;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {

        String email = request.getEmail();
        Role role = request.getRole();
        Provider provider = request.getProvider();
        String name = request.getName();

        Long memberId;

        if(role == Role.TEACHER) {
            teacherRepository.findByEmailAndProvider(email, provider)
                    .ifPresent((teacher) -> { throw new RestApiException(CommonErrorCode.BAD_REQUEST); });

            Teacher teacher = new Teacher(email, role, provider, name);
            teacherRepository.save(teacher);

            memberId = teacher.getId();
        } else {
            studentRepository.findByEmailAndProvider(email, provider)
                    .ifPresent((student) -> { throw new RestApiException(CommonErrorCode.BAD_REQUEST); });

            Student student = new Student(email, role, provider, name);
            studentRepository.save(student);

            memberId = student.getId();
        }

        String accessToken = jwtTokenProvider.createAccessToken(email, memberId, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        return SignUpResponse.builder()
                .id(memberId)
                .role(role)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
