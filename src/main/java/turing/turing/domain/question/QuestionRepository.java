package turing.turing.domain.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import turing.turing.domain.student.Student;
import turing.turing.domain.teacher.Teacher;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q left join fetch q.comments WHERE q.id = :id")
    Optional<Question> findWithCommentsById(@Param("id") Long id);

    // 선생님용 질문 조회
    @Query(value = "select q from Question q join q.studyRoom s where s.teacher = :teacher")
    List<Question> findAllQuestionByTeacher(@Param(value = "teacher") Teacher teacher);

    // 학생용 질문 조회
    @Query(value = "select q from Question q join q.studyRoom s where s.student = :student")
    List<Question> findAllQuestionByStudent(@Param(value = "student") Student student);

    @Query(value = "select q from Question q join fetch q.studyRoom s where q.id = :questionId")
    Optional<Question> findWithStudyRoomById(@Param(value = "questionId") Long questionId);
}
