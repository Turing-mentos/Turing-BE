package turing.turing.domain.studyRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turing.turing.domain.teacher.Teacher;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    boolean existsByTeacherId(Long teacherId);
}
