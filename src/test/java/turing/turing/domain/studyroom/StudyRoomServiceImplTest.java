package turing.turing.domain.studyroom;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import turing.turing.domain.IntegrationTestSupport;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.code.ConnectionCode;
import turing.turing.domain.code.ConnectionCodeRepository;
import turing.turing.domain.exam.Exam;
import turing.turing.domain.exam.ExamRepository;
import turing.turing.domain.exam.ExamService;
import turing.turing.domain.exam.dto.CreateExamRequest;
import turing.turing.domain.exam.dto.UpdateExamRequest;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.schedule.ScheduleService;
import turing.turing.domain.schedule.dto.ScheduleDto;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.studyRoom.StudyRoomService;
import turing.turing.domain.studyRoom.StudyRoomServiceImpl;
import turing.turing.domain.studyRoom.dto.BaseTemplateDto;
import turing.turing.domain.studyRoom.dto.request.StudyRoomCreateReqDto;
import turing.turing.domain.studyRoom.dto.request.StudyRoomUpdateReqDto;
import turing.turing.domain.studyRoom.dto.response.DetailedStudyRoomResDto;
import turing.turing.domain.studyRoom.dto.response.StudyRoomResDto;
import turing.turing.domain.studyRoom.dto.response.SubjectAndTeacherResDto;
import turing.turing.domain.studyTime.StudyTime;
import turing.turing.domain.studyTime.StudyTimeRepository;
import turing.turing.domain.studyTime.dto.StudyTimeReqDto;
import turing.turing.domain.studyTime.dto.StudyTimeResDto;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.StudyRoomErrorCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StudyRoomServiceImplTest extends IntegrationTestSupport{

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudyRoomRepository studyRoomRepository;
    @Autowired
    private StudyRoomService studyRoomService;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ConnectionCodeRepository connectionCodeRepository;
    @Autowired
    private StudyTimeRepository studyTimeRepository;

    private Teacher teacher;
    private Student student;
    private StudyRoomCreateReqDto studyRoomCreateReqDto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "선생", "김");  // 선생님
        teacherRepository.save(teacher);

        ArrayList<StudyTimeReqDto> studyTimes = new ArrayList<>(List.of(new StudyTimeReqDto(1, LocalTime.of(12, 0), LocalTime.of(14, 0))));

        studyRoomCreateReqDto = new StudyRoomCreateReqDto(  // 과외공간 생성 DTO
                "학생", "김", "학교", "1", "국어", 8, 20000, studyTimes, LocalDate.of(2020, 1, 1)
        );
    }

    @DisplayName("새로운 학생에 대한 과외공간을 추가할 수 있다.")
    @Test
    void createStudyRoom() {
        // given
        // (@BeforeEach setUp 메서드에서 수행함)

        // when
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);

        // then
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId).get();
        assertThat(studyRoom).extracting("teacher", "linkStatus", "subject", "baseSession", "wage")
                .contains(teacher, false, "국어", 8, 20000);  // 과외 공간 검증
        assertThat(studyRoom.getStudent()).extracting("firstName", "lastName", "school", "year")
                .contains("학생", "김", "학교", "1");  // 임시 학생 검증
        assertThat(studyRoom.getStudyTimes().get(0)).extracting("day", "startTime", "endTime")
                .contains(1, LocalTime.of(12, 0), LocalTime.of(14, 0));  // 수업 시간 검증
    }

    @DisplayName("과외공간을 수정할 수 있다.")
    @Test
    void updateStudyRoom() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId).get();

        List<StudyTimeReqDto> newStudyTimes = new ArrayList<>(List.of(new StudyTimeReqDto(2, LocalTime.of(14, 0), LocalTime.of(16, 0))));
        StudyRoomUpdateReqDto studyRoomUpdateReqDto = new StudyRoomUpdateReqDto("수학", newStudyTimes, 4, 30000);

        // when
        studyRoomService.updateStudyRoom(studyRoomId, studyRoomUpdateReqDto);

        // then
        assertThat(studyRoom).extracting("subject", "baseSession", "wage")
                .contains("수학", 4, 30000);  // 과외 공간 검증
        assertThat(studyRoom.getStudyTimes().get(0)).extracting("day", "startTime", "endTime")
                .contains(2, LocalTime.of(14, 0), LocalTime.of(16, 0));  // 수업 시간 검증
    }

    @DisplayName("과외공간을 삭제할 수 있다.")
    @Test
    void deleteStudyRoom() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);

        // when
        studyRoomService.deleteStudyRoom(studyRoomId);

        // then
        Optional<StudyRoom> studyRoom = studyRoomRepository.findById(studyRoomId);
        assertThat(studyRoom).isEmpty();
    }

    @DisplayName("연결 코드를 생성 및 조회할 수 있다.")
    @Test
    void getConnectionCode() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId).get();

        // when
        Integer code = studyRoomService.getConnectionCode(studyRoomId);  // 연결 코드 생성
        Integer codeAgain = studyRoomService.getConnectionCode(studyRoomId);  // 이미 생성된 연결 코드 조회

        // then
        ConnectionCode connectionCode = connectionCodeRepository.findByStudyRoom(studyRoom).get();
        assertThat(code).isEqualTo(connectionCode.getCode());  // 연결 코드 검증
        assertThat(code).isEqualTo(codeAgain);  // 조회한 연결 코드 검증
    }

    @DisplayName("연결 코드를 생성 및 조회할 때, 이미 연결된 과외 공간인 경우 예외가 발생한다.")
    @Test
    void getConnectionCodeFailed() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId).get();
        Integer code = studyRoomService.getConnectionCode(studyRoomId);  // 연결 코드 생성

        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생", "김");
        studentRepository.save(student);  // 학생 가입

        studyRoomService.connectTeacherStudent(student.getId(), code);  // 선생님 - 학생 연결

        // when
        // then
        assertThatThrownBy(() -> studyRoomService.getConnectionCode(studyRoomId))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode").isEqualTo(StudyRoomErrorCode.ALREADY_CONNECTED_STUDY_ROOM);  // 예외 발생 확인
    }


    @DisplayName("연결 코드를 통해 선생님 정보를 조회할 수 있다.")
    @Test
    void getTeacherByCode() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId).get();
        Integer code = studyRoomService.getConnectionCode(studyRoomId);

        // when
        SubjectAndTeacherResDto subjectAndTeacherResDto = studyRoomService.getTeacherByCode(code);

        // then
        assertThat(subjectAndTeacherResDto).extracting("subject", "teacherFirstName", "teacherLastName")
                .contains("국어", "선생", "김");
    }

    @DisplayName("연결 코드를 통해 선생님과 학생을 연결할 수 있다.")
    @Test
    void connectTeacherStudent() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId).get();
        Integer code = studyRoomService.getConnectionCode(studyRoomId);

        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생", "김");  // 가입 학생
        studentRepository.save(student);

        // when
        studyRoomService.connectTeacherStudent(student.getId(), code);

        // then
        assertThat(studyRoom.getLinkStatus()).isTrue();
        assertThat(studyRoom.getStudent()).isEqualTo(student);
    }

    @DisplayName("선생님과 학생의 연결을 해제할 수 있다.")
    @Test
    void disconnectTeacherStudent() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId).get();
        Integer code = studyRoomService.getConnectionCode(studyRoomId);

        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생", "김");  // 가입 학생
        studentRepository.save(student);

        studyRoomService.connectTeacherStudent(student.getId(), code);  // 연결

        // when
        studyRoomService.disconnectTeacherStudent(studyRoomId);  // 연결 해제

        // then
        assertThat(studyRoom.getLinkStatus()).isFalse();
        assertThat(studyRoom.getStudent()).isNotEqualTo(student);  // 기존 학생 객체와 달라야 한다
        assertThat(studyRoom.getStudent()).extracting("firstName", "lastName", "school", "year")  // 그럼에도 정보는 유지되어야 한다
                .contains("학생", "김", "학교", "1");
    }

    @DisplayName("선생님은 진행 중인 수업을 조회할 수 있다.")
    @Test
    void getStudyRoomsForTeacher(){
        // given
        Long studyRoomId1 = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        Long studyRoomId2 = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);

        // when
        List<StudyRoomResDto> studyRoomResDtoList = studyRoomService.getStudyRooms(Role.TEACHER, teacher.getId());

        // then
        assertThat(studyRoomResDtoList).hasSize(2);
    }

    @DisplayName("학생은 진행 중인 수업을 조회할 수 있다.")
    @Test
    void getStudyRoomsForStudent(){
        // given
        Long studyRoomId1 = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        Long studyRoomId2 = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        Integer code1 = studyRoomService.getConnectionCode(studyRoomId1);
        Integer code2 = studyRoomService.getConnectionCode(studyRoomId2);

        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생", "김");  // 가입 학생
        studentRepository.save(student);

        studyRoomService.connectTeacherStudent(student.getId(), code1);  // 두 과외 공간을 가입 학생과 연결
        studyRoomService.connectTeacherStudent(student.getId(), code2);

        // when
        List<StudyRoomResDto> studyRoomResDtoList = studyRoomService.getStudyRooms(Role.STUDENT, student.getId());

        // then
        assertThat(studyRoomResDtoList).hasSize(2);
    }

    @DisplayName("선생님은 진행 중인 수업 상세 정보를 조회할 수 있다.")
    @Test
    void getDetailedStudyRooms() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);

        // when
        DetailedStudyRoomResDto detailedStudyRoom = studyRoomService.getDetailedStudyRoom(studyRoomId, Role.TEACHER);

        // then
        assertThat(detailedStudyRoom).extracting("oppositeFirstName", "oppositeLastName", "subject", "baseSession", "wage")
                .contains("학생", "김", "국어", 8, 20000);
        assertThat(detailedStudyRoom.studyTimes().get(0)).extracting("day", "startTime", "endTime")
                .contains(1, LocalTime.of(12, 0), LocalTime.of(14, 0));

        // TODO: 현재회차 및 총회차 계산 로직 검증 필요 (스케줄 완성 후 추가)
        // curSession
        // curBaseSession
        // totalSession
        // totalBaseSession
    }

    @DisplayName("학생은 진행 중인 수업 상세 정보를 조회할 수 있다.")
    @Test
    void getDetailedStudyRoomsForStudent() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);

        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생", "김");  // 가입 학생
        studentRepository.save(student);

        studyRoomService.connectTeacherStudent(student.getId(), studyRoomService.getConnectionCode(studyRoomId));

        // when
        DetailedStudyRoomResDto detailedStudyRoom = studyRoomService.getDetailedStudyRoom(studyRoomId, Role.STUDENT);

        // then
        assertThat(detailedStudyRoom).extracting("oppositeFirstName", "oppositeLastName", "subject", "baseSession", "wage")
                .contains("선생", "김", "국어", 8, 20000);
        assertThat(detailedStudyRoom.studyTimes().get(0)).extracting("day", "startTime", "endTime")
                .contains(1, LocalTime.of(12, 0), LocalTime.of(14, 0));

        // TODO: 현재회차 및 총회차 계산 로직 검증 필요 (스케줄 완성 후 추가)
        // curSession
        // curBaseSession
        // totalSession
        // totalBaseSession
    }

    @DisplayName("중복되지 않는 6자리 연결 코드를 생성할 수 있다.")
    @Test
    void generateCode() {
        // given
        // when
        Integer code1 = studyRoomService.generateCode();
        Integer code2 = studyRoomService.generateCode();

        // then
        assertThat(code1).isBetween(100000, 999999);  // 6자리 코드인지 확인
        assertThat(code1).isNotEqualTo(code2);  // 중복되지 않는 코드인지 확인
    }

    @DisplayName("중복되지 않는 6자리 연결 코드 생성을 maxAttempts번 실패하면 예외가 발생한다.")
    @Test
    void generateCodeFailed() {
        // given
        ConnectionCodeRepository mockRepository = mock(ConnectionCodeRepository.class);
        when(mockRepository.existsByCode(anyInt())).thenReturn(true);  // existsByCode가 항상 true를 반환하도록 설정
        ReflectionTestUtils.setField(studyRoomService, "connectionCodeRepository", mockRepository);  // 테스트를 위해 mock repository를 주입

        // when
        // then
        assertThatThrownBy(() -> studyRoomService.generateCode())
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode").isEqualTo(StudyRoomErrorCode.CONNECTION_CODE_GENERATE_FAILED);  // 예외 발생 확인
        verify(mockRepository, times(10)).existsByCode(anyInt());  // mock 호출 확인

        ReflectionTestUtils.setField(studyRoomService, "connectionCodeRepository", connectionCodeRepository);  // 원래 repository로 복원
    }

    @DisplayName("과외공간의 기본 템플릿 정보를 조회할 수 있다.")
    @Test
    void getBaseTemplate() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);

        // when
        BaseTemplateDto baseTemplate = studyRoomService.getBaseTemplate(studyRoomId);

        // then
        assertThat(baseTemplate).extracting("studyRoomId", "baseSession", "wage")
                .contains(studyRoomId, 8, 20000);
        assertThat(baseTemplate.getStudyTimeList().get(0)).extracting("day", "startTime", "endTime")
                .contains(1, LocalTime.of(12, 0), LocalTime.of(14, 0));
    }

    @DisplayName("과외공간의 기본 템플릿 정보 조회 시, StudyTime이 존재하지 않으면 예외가 발생한다.")
    @Test
    void getBaseTemplateFailed() {
        // given
        Long studyRoomId = studyRoomService.createStudyRoom(teacher.getId(), studyRoomCreateReqDto);
        studyTimeRepository.deleteByStudyRoomId(studyRoomId);  // StudyTime 삭제

        // when
        // then
        assertThatThrownBy(() -> studyRoomService.getBaseTemplate(studyRoomId))  // 예외 발생 확인
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode").isEqualTo(StudyRoomErrorCode.STUDY_TIME_NOT_FOUND);
    }

}