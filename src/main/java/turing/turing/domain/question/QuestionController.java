package turing.turing.domain.question;

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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionPreviewResDto>> getAllQuestions(){

        List<QuestionPreviewResDto> questionList = questionService.getQuestionList(1L);  // 추후 Authentication 정보를 토대로 인자 전달 (role, id)

        return ResponseEntity.ok(questionList);
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<QuestionWithCommentsResDto> getQuestion(@PathVariable Long questionId){

        QuestionWithCommentsResDto questionWithCommentsResDto = questionService.getDetailedQuestion(questionId);

        return ResponseEntity.ok(questionWithCommentsResDto);
    }

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

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable(name = "questionId") Long questionId){

        questionService.deleteQuestion(questionId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/questions/{questionId}/pin")
    public ResponseEntity<Void> pin(@PathVariable(name = "questionId") Long questionId) {

        questionService.pin(questionId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/questions/{questionId}/solve")
    public ResponseEntity<Void> solve(@PathVariable(name = "questionId") Long questionId) {

        questionService.solve(questionId);

        return ResponseEntity.ok().build();
    }
}
