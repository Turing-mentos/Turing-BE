package turing.turing.domain.studyroom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import turing.turing.domain.exam.Exam;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.config.JpaConfig;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
class StudyRoomTest {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudyRoomRepository studyRoomRepository;

    @DisplayName("학생 연결 상태를 true로 변경할 수 있다.")
    @Test
    void connectStudentTest() {
        //given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "선생", "김");  // 선생님
        Student fakeStudent = new Student("null", Role.STUDENT, Provider.KAKAO, "학생", "임시");  // 미가입 학생 (임시 객체)
        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, fakeStudent);  // 과외 공간
        teacherRepository.save(teacher);
        studentRepository.save(fakeStudent);
        studyRoomRepository.save(studyRoom);

        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생", "김");  // 가입 학생
        studentRepository.save(student);

        //when
        studyRoom.connectStudent(student);  // 가입한 학생과의 연결

        //then
        assertThat(studyRoom.getLinkStatus()).isEqualTo(true);  // 연결 상태 검증
        assertThat(studyRoom.getStudent().getEmail()).isEqualTo("student@naver.com");  // 연결된 학생의 이메일이 맞는지 검증
    }

    @DisplayName("학생 연결 상태를 false로 변경할 수 있다.")
    @Test
    void disconnectStudentTest() {
        //given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "선생", "김");  // 선생님
        Student fakeStudent = new Student("null", Role.STUDENT, Provider.KAKAO, "학생", "임시");  // 미가입 학생 (임시 객체)
        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, fakeStudent);  // 과외 공간
        teacherRepository.save(teacher);
        studentRepository.save(fakeStudent);
        studyRoomRepository.save(studyRoom);

        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생", "김");  // 가입 학생
        studentRepository.save(student);
        studyRoom.connectStudent(student);  // 가입한 학생과 연결

        //when
        studyRoom.disconnectStudent(fakeStudent);  // 학생 연결 시도 (미가입 임시 학생 객체와의 연결)

        //then
        assertThat(studyRoom.getLinkStatus()).isEqualTo(false);  // 연결 상태 검증
        assertThat(studyRoom.getStudent().getEmail()).isEqualTo("null");  // 임시 학생의 이메일이 맞는지 검증
    }

    @DisplayName("과외 공간의 필드를 변경할 수 있다.")
    @Test
    void updateStudyRoomTest() {
        //given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "선생", "김");  // 선생님
        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생", "김");  // 가입 학생
        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, student);  // 과외 공간
        teacherRepository.save(teacher);
        studentRepository.save(student);
        studyRoomRepository.save(studyRoom);
        studyRoom.connectStudent(student);  // 가입한 학생과의 연결

        //when
        studyRoom.updateStudyRoom("새로운 과목", 4, 30000);  // 업데이트 시도

        //then
        assertThat(studyRoom.getSubject()).isEqualTo("새로운 과목");  // 연결 상태 검증
        assertThat(studyRoom.getBaseSession()).isEqualTo(4);  // 연결 상태 검증
        assertThat(studyRoom.getWage()).isEqualTo(30000);  // 연결 상태 검증
    }

    @DisplayName("과외 공간에서 학생 이름을 가져올 수 있다.")
    @Test
    void getStudentNameTest() {
        //given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "선생", "김");  // 선생님
        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생", "김");  // 가입 학생
        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, student);  // 과외 공간
        teacherRepository.save(teacher);
        studentRepository.save(student);
        studyRoomRepository.save(studyRoom);
        studyRoom.connectStudent(student);  // 가입한 학생과의 연결

        //when
        String studentName = studyRoom.getStudentName();

        //then
        assertThat(studentName).isEqualTo("김학생");
    }


}