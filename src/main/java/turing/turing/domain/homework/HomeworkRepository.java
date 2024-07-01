package turing.turing.domain.homework;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turing.turing.domain.notebook.Notebook;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    List<Homework> findAllByNotebookId(Long notebookId);

    @Query("select h from Homework h "
            + "where h.notebook in :notebooks ")
    List<Homework> findAllByNotebooks(@Param("notebooks") List<Notebook> notebooks, Sort sort);

}
