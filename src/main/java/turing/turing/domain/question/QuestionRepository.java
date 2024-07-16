package turing.turing.domain.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.teacher.Teacher;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 선생님용 질문 조회
    @Query(value = "select q from Question q join q.studyRoom s where s.teacher = :teacher")
    List<Question> findAllQuestionByTeacher(@Param(value = "teacher") Teacher teacher);

    @Query(value = "select q from Question q join fetch q.studyRoom s where q.id = :questionId")
    Optional<Question> findWithStudyRoomById(@Param(value = "questionId") Long questionId);
}
