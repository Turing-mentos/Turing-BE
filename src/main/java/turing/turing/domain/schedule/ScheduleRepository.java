package turing.turing.domain.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.studyRoom.StudyRoom;



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

    List<Schedule> findAllByStudyRoomOrderByDate(StudyRoom studyRoom);

    @NonNull
    @Query("select s from Schedule s "
            + "join fetch StudyRoom sr "
            + "where s.id = :scheduleId")
    Optional<Schedule> findById(@NonNull @Param("scheduleId") Long scheduleId);

    @Query("select s.startTime from Schedule s where s.date=:date and s.studyRoom.id=:studyRoomId")
    LocalTime findScheduleByDateAndStudyRoom(@Param("date") LocalDate date, @Param("studyRoomId") Long studyRoomId);

    @Query("select s.startTime from Schedule s "
            + "join Notebook n on s.id=n.schedule.id "
            + "where n.id=:notebookId "
            + "and s.date=:date")
    LocalTime findScheduleByDateAndNotebook(@Param("date") LocalDate date, @Param("notebookId") Long notebookId);

    @Query("select s.date from Schedule s where s.studyRoom.id="
            + "(select s2.studyRoom.id from Schedule s2 where s2.id=:scheduleId) "
            + "and s.date > (select s3.date from Schedule s3 where s3.id=:scheduleId) "
            + "order by s.date asc")
    List<LocalDate> findNextDateById(@Param("scheduleId") Long scheduleId, Pageable pageable);

    //리포트생성 시 필요
    @Query(value = "SELECT * FROM schedule WHERE study_room_id = :studyRoomId AND date < CURDATE() ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Schedule searchByStudyRoomIdAndLatest(@Param("studyRoomId") Long studyRoomId);

    @Query(value = "SELECT * FROM schedule " +
            "WHERE date >= (" +
            "    SELECT MAX(date) FROM schedule " +
            "    WHERE date < CURDATE() " +
            "      AND session = 1" +
            ") " +
            "AND date <= (" +
            "    SELECT MIN(date) FROM schedule " +
            "    WHERE date >= CURDATE() " +
            "      AND session = :baseSession" +
            ") " +
            "ORDER BY date ASC", nativeQuery = true)
    List<Schedule> findSchedulesInRange(@Param("baseSession") int baseSession);

}
