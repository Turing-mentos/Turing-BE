package turing.turing.domain.notebook;

import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
