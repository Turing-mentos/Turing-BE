package turing.turing.domain.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.List;
import lombok.NonNull;
import turing.turing.domain.schedule.dto.ScheduleDto;
import turing.turing.domain.studyRoom.StudyRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.studyRoom.StudyRoom;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  
    List<Schedule> findAllByStudyRoomOrderByDate(StudyRoom studyRoom);

    @Query("select new turing.turing.domain.schedule.dto.ScheduleDto(s.id, s.date, s.studentName, s.subject, s.session, s.startTime, s.endTime, sr.id, sr.baseSession) from Schedule s "
            + "join fetch StudyRoom sr on s.studyRoom.id in :studyRoomIds "
            + "where month(s.date)=:month and year(s.date)=:year "
            + "order by s.date")
    List<ScheduleDto> findAllByDateAndStudyRoomIds(@Param("month") int month, @Param("year") int year, @Param("studyRoomIds") List<Long> studyRoomsIds);

    @Query("select new turing.turing.domain.schedule.dto.ScheduleDto(s.id, s.date, s.studentName, s.subject, s.session, s.startTime, s.endTime, sr.id, sr.baseSession) from Schedule s "
            + "join fetch StudyRoom sr "
            + "where s.id=:scheduleId")
    Optional<ScheduleDto> findByScheduleId(@Param("scheduleId") Long scheduleId);


    @Query("select distinct s from Schedule s "
            + "where (s.date between :modifiedDate and "
                + "(select s1.date from Schedule s1 where s1.id=:scheduleId)) "
            + "or (s.date between "
                + "(select s1.date from Schedule s1 where s1.id = :scheduleId) and :modifiedDate) "
            + "or s.id=:scheduleId "
            + " order by s.date asc")
    List<Schedule> findAllByIdAndDate(@Param("scheduleId") Long scheduleId, @Param("modifiedDate") LocalDate modifiedDate);

    @Modifying
    @Query("update Schedule s set s.session = s.session + 1 where s.id in :scheduleIds")
    Void postponeSchedules(@Param("scheduleIds") List<Long> scheduleIds);

    @Modifying
    @Query("update Schedule s set s.session = s.session - 1 where s.id in :scheduleIds")
    Void moveUpSchedules(@Param("scheduleIds") List<Long> scheduleIds);

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
