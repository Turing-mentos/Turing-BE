package turing.turing.domain.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.notebook.Notebook;

import java.time.LocalDate;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notebook n WHERE n.deadline = :targetDate")
    List<Notebook> findNoteBookByDate(@Param("targetDate") LocalDate targetDate);
}
