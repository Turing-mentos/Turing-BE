package turing.turing.domain.exam;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;
import turing.turing.domain.student.Student;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.config.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
class ExamTest {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudyRoomRepository studyRoomRepository;

    @DisplayName("시험 정보에서 과외공간 Id를 조회할 수 있다.")
    @Test
    void getStudyRoomId() {
        //given
        Teacher teacher = new Teacher("teacher@naver.com", Role.TEACHER, Provider.KAKAO, "범준", "김");
        Student student = new Student("student@naver.com", Role.STUDENT, Provider.KAKAO, "학생이름",
                "학생성");

        StudyRoom studyRoom = new StudyRoom("과목", 8, 20000, teacher, student);
        teacherRepository.save(teacher);
        studentRepository.save(student);
        Long savedId = studyRoomRepository.save(studyRoom).getId();

        //when
        Exam exam = new Exam(1L, "시험명", LocalDate.of(2024, 11, 16), LocalDate.of(2024, 11, 17),
                "학생명", studyRoom);

        //then
        assertThat(exam.getStudyRoomId()).isEqualTo(savedId);
    }
}