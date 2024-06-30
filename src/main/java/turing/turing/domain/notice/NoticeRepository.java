package turing.turing.domain.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.notebook.Notebook;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notebook n WHERE " +
            "YEAR(n.deadline) = YEAR(:targetDate) AND " +
            "MONTH(n.deadline) = MONTH(:targetDate) AND " +
            "DAY(n.deadline) = DAY(:targetDate) AND " +
            "HOUR(n.deadline) = HOUR(:targetDate) AND " +
            "MINUTE(n.deadline) = MINUTE(:targetDate)")
    List<Notebook> serachNoteBookByDate(@Param("targetDate") Timestamp targetDate);
}
