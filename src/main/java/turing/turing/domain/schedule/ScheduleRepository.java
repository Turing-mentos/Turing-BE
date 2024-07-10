package turing.turing.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.studyRoom.StudyRoom;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

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

    List<Schedule> findAllByStudyRoomOrderByDate(StudyRoom studyRoom);

}
