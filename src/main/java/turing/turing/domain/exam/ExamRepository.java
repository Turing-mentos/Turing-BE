package turing.turing.domain.exam;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("select e from Exam e "
            + "join fetch StudyRoom s "
            + "where e.id=:examId")
    Optional<Exam> findExamWithStudyRoomById(@Param("examId") Long examId);
}
