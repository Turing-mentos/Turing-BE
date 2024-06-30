package turing.turing.domain.homework;

import org.springframework.data.jpa.repository.JpaRepository;
import turing.turing.domain.notebook.Notebook;

import java.util.List;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findAllByNotebook(Notebook notebook);
}
