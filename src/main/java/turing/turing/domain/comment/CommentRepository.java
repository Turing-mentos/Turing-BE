package turing.turing.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import turing.turing.domain.question.Question;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByQuestion(Question question);
}
