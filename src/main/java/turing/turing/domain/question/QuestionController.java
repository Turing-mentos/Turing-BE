package turing.turing.domain.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import turing.turing.domain.question.dto.response.QuestionPreviewResDto;
import turing.turing.domain.question.dto.response.QuestionWithCommentsResDto;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<QuestionPreviewResDto>> getAllQuestions(){
        List<QuestionPreviewResDto> questionList = questionService.getQuestionList(1L);  // 추후 Authentication 정보를 토대로 인자 전달 (role, id)
        return ResponseEntity.ok(questionList);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionWithCommentsResDto> getQuestion(@PathVariable(name = "questionId") Long questionId){
        QuestionWithCommentsResDto questionWithCommentsResDto = questionService.getDetailedQuestion(questionId);
        return ResponseEntity.ok(questionWithCommentsResDto);
    }
}
