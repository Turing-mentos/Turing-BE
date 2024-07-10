package turing.turing.domain.question;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import turing.turing.domain.comment.Comment;
import turing.turing.domain.comment.CommentRepository;
import turing.turing.domain.question.dto.request.QuestionReqDto;
import turing.turing.domain.question.dto.response.QuestionCreateResDto;
import turing.turing.domain.question.dto.response.QuestionPreviewResDto;
import turing.turing.domain.question.dto.response.QuestionWithCommentsResDto;
import turing.turing.domain.student.StudentRepository;
import turing.turing.domain.studyRoom.StudyRoom;
import turing.turing.domain.studyRoom.StudyRoomRepository;
import turing.turing.domain.teacher.Teacher;
import turing.turing.domain.teacher.TeacherRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;
import turing.turing.global.s3.S3Service;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final TeacherRepository teacherRepository;
    private final QuestionRepository questionRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final CommentRepository commentRepository;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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
    public QuestionCreateResDto createQuestion(Long studyRoomId, MultipartFile file, QuestionReqDto questionReqDto){
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        String fileUrl = null;
        if(file != null && !file.isEmpty())
            fileUrl = s3Service.uploadFile(file);  // 파일이 존재할 경우 S3에 업로드 후 URL 저장

        Question question = Question.builder()
                .title(questionReqDto.title())
                .category(questionReqDto.category())
                .content(questionReqDto.content())
                .questionImage(fileUrl)
                .studyRoom(studyRoom)
                .build();

        questionRepository.save(question);

        return QuestionCreateResDto.builder()
                .questionId(question.getId())
                .senderId(studyRoom.getStudent().getId())
                .senderRole("STUDENT")  // 추후 Role로 변경
                .receiverId(studyRoom.getTeacher().getId())
                .receiverRole("TEACHER")  // 추후 Role로 변경
                .build();
    }

    @Transactional
    public void deleteQuestion(Long questionId){
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        // 이미지가 존재한다면 해당 이미지를 S3에서 삭제 후 질문 삭제
        if(question.getQuestionImage() != null && !question.getQuestionImage().isEmpty()){
            s3Service.deleteFile(question.getQuestionImage());
        }
        questionRepository.delete(question);
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
