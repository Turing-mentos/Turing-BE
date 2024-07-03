package turing.turing.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.studyRoom.StudyRoom;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE " +
            "DATE(s.endTime) = :targetDate AND " +
            "HOUR(s.endTime) = :targetHour AND " +
            "MINUTE(s.endTime) = :targetMinute")
    List<Schedule> searchScheduleByDateAndTime(
            @Param("targetDate") LocalDate targetDate,
            @Param("targetHour") int targetHour,
            @Param("targetMinute") int targetMinute);

    List<Schedule> findByDateAndEndTime(LocalDate date, LocalTime endDate);

    @Query("SELECT s FROM Schedule s WHERE s.studyRoom = :studyRoom AND s.date <= CURRENT_DATE ORDER BY s.date DESC")
    Schedule searchByStudyRoomAndLatestDate(@Param("studyRoom") StudyRoom studyRoom);
}
