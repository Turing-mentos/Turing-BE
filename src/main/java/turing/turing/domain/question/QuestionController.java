package turing.turing.domain.question;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import turing.turing.domain.question.dto.request.QuestionReqDto;
import turing.turing.domain.question.dto.response.QuestionCreateResDto;
import turing.turing.domain.question.dto.response.QuestionPreviewResDto;
import turing.turing.domain.question.dto.response.QuestionWithCommentsResDto;

import java.net.URI;
import java.util.List;

@Tag(name = "Question", description = "질문")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "질문 전체 조회")
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionPreviewResDto>> getAllQuestions(){

        List<QuestionPreviewResDto> questionList = questionService.getQuestionList(1L);  // 추후 Authentication 정보를 토대로 인자 전달 (role, id)

        return ResponseEntity.ok(questionList);
    }

    @Operation(summary = "질문 상세 조회 & 댓글 조회")
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<QuestionWithCommentsResDto> getQuestion(@PathVariable Long questionId){

        QuestionWithCommentsResDto questionWithCommentsResDto = questionService.getDetailedQuestion(questionId);

        return ResponseEntity.ok(questionWithCommentsResDto);
    }

    @Operation(summary = "질문 생성 (설명 참고)", description = "comment(application/json)와 file(multipart/form-data)를 전달 받습니다.")
    @PostMapping("/study-rooms/{studyRoomId}/questions")
    public ResponseEntity<Long> uploadQuestion(
            @PathVariable Long studyRoomId,
            @RequestPart(value = "question") QuestionReqDto questionReqDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        QuestionCreateResDto questionCreateResDto = questionService.createQuestion(studyRoomId, questionReqDto, file);
        Long questionId = questionCreateResDto.questionId();
        URI location = URI.create("/api/questions/" + questionId);

        return ResponseEntity.created(location).body(questionId);
    }

    @Operation(summary = "질문 수정 (설명 참고)", description = "comment(application/json)와 file(multipart/form-data)를 전달 받습니다.")
    @PutMapping("/questions/{questionId}")
    public ResponseEntity<Void> updateQuestion(
            @PathVariable Long questionId,
            @RequestPart(value = "question") QuestionReqDto questionReqDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        questionService.updateQuestion(questionId, questionReqDto, file);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "질문 삭제")
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId){

        questionService.deleteQuestion(questionId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "질문 고정 (toggle)")
    @PatchMapping("/questions/{questionId}/pin")
    public ResponseEntity<Void> pin(@PathVariable Long questionId) {

        questionService.pin(questionId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "질문 해결 (toggle)")
    @PatchMapping("/questions/{questionId}/solve")
    public ResponseEntity<Void> solve(@PathVariable Long questionId) {

        questionService.solve(questionId);

        return ResponseEntity.ok().build();
    }
}
