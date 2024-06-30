package turing.turing.domain.homework;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turing.turing.domain.notebook.Notebook;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findAllByNotebook(Notebook notebook);
}
