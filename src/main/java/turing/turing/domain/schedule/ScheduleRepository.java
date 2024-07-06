package turing.turing.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.studyRoom.StudyRoom;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE " +
            "DATE(s.date) = :targetDate AND " +
            "HOUR(s.endTime) = :targetHour AND " +
            "MINUTE(s.endTime) = :targetMinute")
    List<Schedule> searchScheduleByDateAndTime(
            @Param("targetDate") LocalDate targetDate,
            @Param("targetHour") int targetHour,
            @Param("targetMinute") int targetMinute);

    @Query("SELECT s FROM Schedule s WHERE s.studyRoom = :studyRoom AND s.date <= CURRENT_DATE ORDER BY s.date DESC")
    Schedule searchByStudyRoomAndLatestDate(@Param("studyRoom") StudyRoom studyRoom);

    @Query("SELECT COUNT(s) > 0 " +
            "FROM Schedule s " +
            "WHERE s.date > :targetDate " +
            "AND s.studyRoom.id = :studyRoomId")
    boolean existsLatestScheduleAfterDate(@Param("targetDate") LocalDate targetDate,
                                          @Param("studyRoomId") Long studyRoomId);

}
