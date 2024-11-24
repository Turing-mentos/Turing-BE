package turing.turing.domain.exam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static turing.turing.global.exception.errorCode.ExamErrorCode.EXAM_NOT_FOUND;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import turing.turing.domain.IntegrationTestSupport;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.exam.dto.CreateExamRequest;
import turing.turing.domain.exam.dto.UpdateExamRequest;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;

class ExamServiceImplTest extends IntegrationTestSupport{

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudyRoomRepository studyRoomRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamService examService;

    @DisplayName("ExamId를 통해 시험정보를 조회할 수 있다.")
    @Test
    void getExamSchedule() {
        // given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "범준", "김");
        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생이름",
                "학생성");

        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, student);
        teacherRepository.save(teacher);
        studentRepository.save(student);
        Long savedStudyRoomId = studyRoomRepository.save(studyRoom).getId();

        LocalDate startDate = LocalDate.of(2024, 11, 16);
        LocalDate endDate = LocalDate.of(2024, 11, 17);
        Exam exam = new Exam(1L, "시험명", startDate, endDate, "학생명", studyRoom);

        // when
        Long savedId = examRepository.save(exam).getId();

        // then
        assertThat(examService.getExamSchedule(savedId))
                .extracting("examId", "examName", "startDate", "endDate", "studyRoomId")
                .contains(savedId, exam.getExamName(), exam.getStartDate(), exam.getEndDate(), savedStudyRoomId);
    }

    @DisplayName("존재하지 않는 Id로 조회할 경우 예외가 발생한다.")
    @Test
    void getExamScheduleWhenIdDoesNotExist() {
        // given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "범준", "김");
        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생이름",
                "학생성");

        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, student);
        teacherRepository.save(teacher);
        studentRepository.save(student);
        studyRoomRepository.save(studyRoom);

        LocalDate startDate = LocalDate.of(2024, 11, 16);
        LocalDate endDate = LocalDate.of(2024, 11, 17);
        Exam exam = new Exam(1L, "시험명", startDate, endDate, "학생명", studyRoom);

        Long savedId = examRepository.save(exam).getId();

        // when // then
        assertThatThrownBy(() -> examService.getExamSchedule(savedId + 1))
                .isInstanceOf(RestApiException.class)
                .extracting("ErrorCode").isEqualTo(EXAM_NOT_FOUND);

    }

    @DisplayName("시험일정을 생성할 수 있다.")
    @Test
    void createExamSchedules() {
        // given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "범준", "김");
        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생이름",
                "학생성");

        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, student);
        teacherRepository.save(teacher);
        studentRepository.save(student);
        Long savedStudyRoomId = studyRoomRepository.save(studyRoom).getId();

        LocalDate startDate = LocalDate.of(2024, 11, 16);
        LocalDate endDate = LocalDate.of(2024, 11, 17);
        CreateExamRequest createExamRequest = CreateExamRequest.builder()
                .examName("시험명")
                .startDate(startDate)
                .endDate(endDate)
                .studyRoomId(savedStudyRoomId)
                .build();
        CustomUserDetails customUserDetails = new CustomUserDetails(teacher.getEmail())
                .role(Role.TEACHER)
                .memberId(teacher.getId())
                .provider(teacher.getProvider());

        // when
        Long savedId = examService.createExamSchedules(customUserDetails, createExamRequest).getExamId();

        // then
        Exam exam = examRepository.findById(savedId).get();
        assertThat(exam).isNotNull();
        assertThat(exam).extracting("id", "examName", "startDate", "endDate", "studentName")
                .contains(savedId, "시험명", startDate, endDate);
        assertThat(exam.getStudyRoomId()).isEqualTo(savedStudyRoomId);
    }

    @DisplayName("요청을 받아 시험일정을 수정할 수 있다.")
    @Test
    void modifyExamSchedule() {
        // given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "범준", "김");
        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생이름",
                "학생성");

        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, student);
        teacherRepository.save(teacher);
        studentRepository.save(student);
        Long savedStudyRoomId = studyRoomRepository.save(studyRoom).getId();
        LocalDate startDate = LocalDate.of(2024, 11, 16);
        LocalDate endDate = LocalDate.of(2024, 11, 17);

        Exam exam = new Exam(1L, "시험명", startDate, endDate, "학생명", studyRoom);
        Long savedId = examRepository.save(exam).getId();

        LocalDate updatedStartDate = LocalDate.of(2024,11,26);
        LocalDate updatedEndDate = LocalDate.of(2024,11,27);
        UpdateExamRequest request = UpdateExamRequest.builder()
                .examId(savedId)
                .examName("변경시험명")
                .startDate(updatedStartDate)
                .endDate(updatedEndDate)
                .studyRoomId(savedStudyRoomId)
                .build();

        // when
        Long updatedId = examService.updateExamSchedule(request);

        // then
        Exam updatedExam = examRepository.findById(updatedId).get();
        assertThat(updatedExam).extracting("id", "examName", "startDate", "endDate")
                .contains(savedId, "변경시험명",updatedStartDate,updatedEndDate);
        assertThat(updatedExam.getStudyRoom()).isEqualTo(studyRoom);
    }

    @DisplayName("시험일정을 삭제할 수 있다.")
    @Test
    void deleteExamSchedule() {
        // given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "범준", "김");
        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생이름",
                "학생성");

        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, student);
        teacherRepository.save(teacher);
        studentRepository.save(student);
        studyRoomRepository.save(studyRoom);

        LocalDate startDate = LocalDate.of(2024, 11, 16);
        LocalDate endDate = LocalDate.of(2024, 11, 17);

        Exam exam = new Exam(1L, "시험명", startDate, endDate, "학생명", studyRoom);
        Long savedId = examRepository.save(exam).getId();

        // when
        examService.deleteExamSchedule(savedId);

        // then
        Optional<Exam> deletedExam = examRepository.findById(savedId);
        assertThat(deletedExam).isEmpty();
    }
}