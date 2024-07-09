package turing.turing.domain.studyRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.student.Student;
import turing.turing.domain.teacher.Teacher;

import java.util.List;
import java.util.Optional;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {

    @Query("select sr from StudyRoom sr " +
            "join fetch sr.student s " +
            "where sr.teacher = :teacher")
    List<StudyRoom> findAllWithStudentByTeacher(@Param(value = "teacher") Teacher teacher);

    @Query("select sr from StudyRoom sr " +
            "join fetch sr.student s " +
            "where sr.id = :studyRoomId")
    Optional<StudyRoom> findWithStudentById(@Param(value = "studyRoomId") Long studyRoomId);

    @Query("select distinct sr from StudyRoom sr " +
            "join fetch sr.studyTimes st " +
            "join fetch sr.student s " +
            "where sr.id = :studyRoomId")
    Optional<StudyRoom> findWithAllStudyTimeAndStudentById(@Param(value = "studyRoomId") Long studyRoomId);

    @Query("SELECT sr FROM StudyRoom sr WHERE sr.teacher.id = :teacherId AND sr.student.id = :studentId")
    StudyRoom findByTeacherIdAndStudentId(@Param(value = "teacherId") Long teacherId, @Param(value = "studentId") Long studentId);
}
