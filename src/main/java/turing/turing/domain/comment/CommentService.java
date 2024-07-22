package turing.turing.domain.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import turing.turing.domain.comment.dto.request.CommentReqDto;
import turing.turing.domain.comment.dto.response.CommentCreateResDto;
import turing.turing.domain.question.Question;
import turing.turing.domain.question.QuestionRepository;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;
import turing.turing.global.s3.S3Service;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final S3Service s3Service;

    @Transactional
    public CommentCreateResDto createComment(String role, Long questionId, CommentReqDto commentReqDto, MultipartFile file) {  // Role Enum 클래스 적용 필요 !

        Question question = questionRepository.findWithStudyRoomById(questionId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        Long teacherId = question.getStudyRoom().getTeacher().getId();
        Long studentId = question.getStudyRoom().getStudent().getId();
        boolean isTeacher = (role == "TEACHER");  // 추후 Enum 변경

        String fileUrl = null;
        if(file != null && !file.isEmpty())
            fileUrl = s3Service.uploadFile(file);  // 파일이 존재할 경우 S3에 업로드 후 URL 저장

        // 댓글 생성
        Comment comment = Comment.builder()
                .role(role)
                .memberId(isTeacher ? teacherId : studentId)
                .content(commentReqDto.content())
                .imageUrl(fileUrl)
                .question(question)
                .build();

        Comment savedComment = commentRepository.save(comment);
        question.increaseCommentCount();

        // 이 부분 Role Enum 클래스 리턴하는 로직으로 변경 필요 !
        return CommentCreateResDto.builder()
                .commentId(savedComment.getId())
                .senderRole(isTeacher ? "TEACHER" : "STUDENT")
                .senderId(isTeacher ? teacherId : studentId)
                .receiverRole(isTeacher ? "STUDENT" : "TEACHER")
                .receiverId(isTeacher ? studentId : teacherId)
                .questionId(questionId)
                .build();
    }

    @Transactional
    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.NOT_FOUND));

        // 이미지가 존재한다면 해당 이미지를 S3에서 삭제 후 댓글 삭제
        if(comment.getImageUrl() != null && !comment.getImageUrl().isEmpty())
            s3Service.deleteFile(comment.getImageUrl());

        Question question = comment.getQuestion();
        commentRepository.delete(comment);
        question.decreaseCommentCount();
    }
}
