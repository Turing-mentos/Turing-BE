package turing.turing.domain.notebook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.schedule.Schedule;

import java.sql.Timestamp;
import java.util.List;

public interface NotebookRepository extends JpaRepository<Notebook, Long> {
    @Query("SELECT n FROM Notebook n WHERE YEAR(n.deadline) = YEAR(:targetDate) " +
            "AND MONTH(n.deadline) = MONTH(:targetDate) " +
            "AND DAY(n.deadline) = DAY(:targetDate) " +
            "AND HOUR(n.deadline) = HOUR(:targetDate) " +
            "AND MINUTE(n.deadline) = MINUTE(:targetDate)")
    List<Notebook> searchNotebooksByDate(@Param("targetDate") Timestamp targetDate);

    Notebook findBySchedule(Schedule schedule);
}
