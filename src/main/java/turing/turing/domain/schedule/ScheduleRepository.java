package turing.turing.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    //리포트생성 시 필요
    @Query(value = "SELECT * FROM schedule WHERE study_room_id = :studyRoomId AND date < CURDATE() ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Schedule searchByStudyRoomIdAndLatest(@Param("studyRoomId") Long studyRoomId);
    @Query(value = "SELECT * FROM Schedule s " +
            "WHERE s.date >= CURRENT_DATE " +  // 오늘 이후의 날짜
            "ORDER BY s.date ASC " +
            "LIMIT :baseSession",
            nativeQuery = true)
    List<Schedule> findSchedulesAfterToday(@Param("baseSession") int baseSession);

}
