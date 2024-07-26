package turing.turing.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.auth.jwt.JwtTokenProvider;
import turing.turing.domain.member.dto.Profile;
import turing.turing.domain.member.dto.SignUpRequest;
import turing.turing.domain.member.dto.SignUpResponse;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;
import turing.turing.global.exception.errorCode.UserErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {

        String email = request.getEmail();
        Role role = request.getRole();
        Provider provider = request.getProvider();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        Long memberId;

        if(role == Role.TEACHER) {
            teacherRepository.findByEmailAndProvider(email, provider)
                    .ifPresent((teacher) -> { throw new RestApiException(UserErrorCode.USER_ALREADY_EXISTS); });

            Teacher teacher = new Teacher(email, role, provider, firstName, lastName);
            teacherRepository.save(teacher);

            memberId = teacher.getId();
        } else {
            studentRepository.findByEmailAndProvider(email, provider)
                    .ifPresent((student) -> { throw new RestApiException(UserErrorCode.USER_ALREADY_EXISTS); });

            Student student = new Student(email, role, provider, firstName, lastName);
            studentRepository.save(student);

            memberId = student.getId();
        }

        String accessToken = jwtTokenProvider.createAccessToken(email, memberId, role, provider);
        String refreshToken = jwtTokenProvider.createRefreshToken(email, memberId, role, provider);

        return SignUpResponse.builder()
                .id(memberId)
                .role(role)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void deleteMember(CustomUserDetails user) {
        Long memberId = user.getMemberId();
        Role role = user.getRole();

        String redisId = jwtTokenProvider.createRedisId(memberId, role);
        jwtTokenProvider.deleteRefreshToken(redisId);

        if (role.equals(Role.TEACHER)) {
            teacherRepository.deleteById(memberId);
            return;
        }

        // 학생의 경우 연결된 모든 과외공간에 대하여 학생 연결 해제 로직 적용
        studyRoomRepository.findAllWIthStudentByStudentId(memberId)
                .forEach(studyRoom -> {
                    // 기존 학생의 정보를 통해 nonSignedUpStudent 생성
                    Student nonSignUpStudent = new Student(
                            studyRoom.getStudent().getFirstName(),
                            studyRoom.getStudent().getLastName(),
                            studyRoom.getStudent().getSchool(),
                            studyRoom.getStudent().getYear(),
                            studyRoom.getStudent().getPhone(),
                            studyRoom.getStudent().getParentPhone()
                    );
                    studentRepository.save(nonSignUpStudent);
                    studyRoom.disconnectStudent(nonSignUpStudent);
                });
        studentRepository.deleteById(memberId);
    }


    @Transactional
    public Boolean updateProfile(CustomUserDetails user, Profile profile) {
        if(user.getRole() == Role.STUDENT){
            Student student = studentRepository.findById(user.getMemberId())
                    .orElseThrow(()->new RestApiException(CommonErrorCode.NOT_FOUND));
            student.updateProfile(profile);
            return true;
        }
        else if(user.getRole() == Role.TEACHER){
            Teacher teacher  = teacherRepository.findById(user.getMemberId())
                    .orElseThrow(()->new RestApiException(CommonErrorCode.NOT_FOUND));
            teacher.updateProfile(profile);
            return true;
        }
        else{
            throw new RestApiException(CommonErrorCode.UNAUTHORIZED_ROLE);
        }
    }
}
