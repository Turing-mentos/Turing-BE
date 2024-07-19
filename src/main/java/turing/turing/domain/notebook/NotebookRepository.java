package turing.turing.domain.notebook;

import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.notebook.dto.HomeworkPercentAllDto;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Long> {

    @Override
    @NonNull
    @Query("select n from Notebook n "
            + "join fetch n.schedule s "
            + "where n.id = :notebookId")
    Optional<Notebook> findById(@NonNull @Param("notebookId") Long notebookId);

    @Query("select n from Notebook n "
            + "join fetch n.schedule s "
            + "where s.studyRoom.id in :studyRoomIds "
            + "and s.date = ("
            + "select min(s2.date) "
            + "from Schedule s2 "
            + "where s2.studyRoom.id = s.studyRoom.id "
            + "and s2.date >= current_date"
            + ") and n.deadline > current_timestamp "
            + "order by n.id asc")
    List<Notebook> findAllByStudyRoomIds(@Param("studyRoomIds") List<Long> studyRoomIds);

    @Query("select n from Notebook n "
            + "join fetch n.schedule s "
            + "where s.studyRoom.id=:studyRoomId "
            + "and (:notebookId is null or n.id<:notebookId) "
            + "and n.deadline < current_date")
    List<Notebook> findAllByStudyRoomId(@Param("studyRoomId") Long studyRoomId, @Param("notebookId") Long notebookId, Pageable pageable);

    @Query(value = "SELECT st.student_id AS id, " +
            "sc.student_name AS name, sc.subject AS subject, " +
            "IFNULL((COUNT(CASE WHEN h.is_done = 1 THEN 1 END) * 100.0 / COUNT(*)), 0) AS completionPercent " +
            "FROM study_room st " +
            "JOIN Schedule sc ON st.study_room_id = sc.study_room_id " +
            "JOIN Notebook n ON sc.schedule_id = n.schedule_id " +
            "JOIN Homework h ON n.notebook_id = h.notebook_id " +
            "WHERE st.teacher_id = :teacherId " +
            "GROUP BY st.student_id, sc.student_name, sc.subject",
            nativeQuery = true)
    List<HomeworkPercentAllDto> getPercentByTeacherId(@Param("teacherId") Long id);

    @Query(value = "SELECT st.teacher_id AS id,  " +
            "t.first_name AS firstName, t.last_name AS lastName, sc.subject AS subject, " +
            "   IFNULL((COUNT(CASE WHEN h.is_done = 1 THEN 1 END) * 100.0 / COUNT(*)), 0) AS completionPercent " +
            "FROM study_room st " +
            "JOIN Teacher t ON t.teacher_id = st.teacher_id " +
            "JOIN Schedule sc ON st.study_room_id = sc.study_room_id " +
            "JOIN Notebook n ON sc.schedule_id = n.schedule_id " +
            "JOIN Homework h ON n.notebook_id = h.notebook_id " +
            "WHERE st.student_id = :studentId " +
            "GROUP BY st.teacher_id, t.first_name, t.last_name, sc.subject",
            nativeQuery = true)
    List<HomeworkPercentAllDto> getPercentByStudentId(@Param("studentId")Long id);
}
