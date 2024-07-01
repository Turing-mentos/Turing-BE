package turing.turing.domain.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

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
    LocalDate findNextDateById(@Param("scheduleId") Long scheduleId, Pageable pageable);

}
