package turing.turing.domain.studyRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.student.Student;
import turing.turing.domain.teacher.Teacher;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    boolean existsByTeacherId(Long teacherId);

    @Query("SELECT s FROM StudyRoom s WHERE s.teacher.id = :teacherId AND s.student.id = :studentId")
    StudyRoom findByTeacherIdAndStudentId(@Param("teacherId") Long teacherId, @Param("studentId") Long studentId);
}
