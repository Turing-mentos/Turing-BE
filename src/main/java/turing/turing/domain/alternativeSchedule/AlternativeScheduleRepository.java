package turing.turing.domain.alternativeSchedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlternativeScheduleRepository extends JpaRepository<AlternativeSchedule, Long> {

    @Query("select ans from AlternativeSchedule ans "
            + "join fetch ans.schedule s "
            + "join fetch s.studyRoom sr "
            + "where s.studyRoom.id in :studyRoomIds "
            + "and s.date >= current_date "
            + "order by sr.id asc, s.id asc")
    List<AlternativeSchedule> findAllByStudyRoomIds(List<Long> studyRoomIds);
}
