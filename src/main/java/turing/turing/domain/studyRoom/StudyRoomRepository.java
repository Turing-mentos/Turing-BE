package turing.turing.domain.studyRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import turing.turing.domain.student.Student;
import turing.turing.domain.teacher.Teacher;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    StudyRoom findByTeacherAndStudent(Teacher teacher, Student student);
}
