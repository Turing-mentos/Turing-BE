package turing.turing.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import turing.turing.domain.studyRoom.StudyRoom;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByStudyRoomOrderByDate(StudyRoom studyRoom);
}
