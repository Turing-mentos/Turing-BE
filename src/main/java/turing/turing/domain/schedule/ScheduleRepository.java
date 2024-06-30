package turing.turing.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.studyRoom.StudyRoom;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findBySession();

    @Query("SELECT s FROM Schedule s WHERE " +
            "DATE(s.endTime) = :targetDate AND " +
            "HOUR(s.endTime) = HOUR(:targetTime) AND " +
            "MINUTE(s.endTime) = MINUTE(:targetTime)")
    List<Schedule> searchScheduleByDateAndTime(@Param("targetDate") LocalDate targetDate, @Param("targetTime") LocalTime targetTime);

    Schedule findByStudyRoom(StudyRoom studyRoom);
}
