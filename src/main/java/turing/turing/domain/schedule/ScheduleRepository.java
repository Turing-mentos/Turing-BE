package turing.turing.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    //리포트생성 시 필요
    @Query(value = "SELECT * FROM Schedule s WHERE s.study_room_id = :studyRoomId AND s.date = CURRENT_DATE ORDER BY s.date DESC LIMIT 1", nativeQuery = true)
    Schedule searchByStudyRoomIdAndLatest(@Param("studyRoomId") Long studyRoomId);
}
