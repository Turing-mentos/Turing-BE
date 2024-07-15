package turing.turing.domain.schedule;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.schedule.dto.ScheduleDto;
import turing.turing.domain.studyRoom.StudyRoom;

import java.util.List;

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
    ScheduleDto findByScheduleId(@Param("scheduleId") Long scheduleId);


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
}
