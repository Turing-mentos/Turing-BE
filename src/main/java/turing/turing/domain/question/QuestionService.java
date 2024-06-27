package turing.turing.domain.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import turing.turing.domain.comment.Comment;
import turing.turing.domain.comment.CommentRepository;
import turing.turing.domain.question.dto.response.QuestionPreviewResDto;
import turing.turing.domain.question.dto.response.QuestionWithCommentsResDto;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final TeacherRepository teacherRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;

    public List<QuestionPreviewResDto> getQuestionList(Long memberId){
        // 선생 학생 구분 필요 (쿼리 때문에 - teacherId? studentId?) -> Authentication을 통해 추후 구분하여 로직 작성

        Teacher teacher = teacherRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));


        List<Question> questionList = questionRepository.findAllQuestionByTeacher(teacher);
        List<QuestionPreviewResDto> questionDtoList = questionList.stream().map(QuestionPreviewResDto::of).toList();
        return questionDtoList;
    }

    public QuestionWithCommentsResDto getDetailedQuestion(Long questionId){
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));
        List<Comment> commentList = commentRepository.findAllByQuestion(question);

        return QuestionWithCommentsResDto.of(question, commentList);
    }

    @Transactional
    public void pin(Long questionId){
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        question.switchPinStatus();
    }

    @Transactional
    public void solve(Long questionId){
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        question.switchSolveStatus();
    }

}
