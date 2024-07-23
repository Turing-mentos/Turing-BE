package turing.turing.domain.exam;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.exam.dto.CreateExamRequest;
import turing.turing.domain.exam.dto.CreateExamResponse;
import turing.turing.domain.exam.dto.ExamDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;


    @GetMapping("/{examId}")
    public ResponseEntity<ExamDto> getExamSchedule(@PathVariable("examId") Long examId) {

        return ResponseEntity.ok(examService.getExamSchedule(examId));
    }

    @PostMapping("/")
    public ResponseEntity<CreateExamResponse> createExamSchedule(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreateExamRequest request) {
        CreateExamResponse response = examService.createExamSchedules(customUserDetails, request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{examId}")
                .buildAndExpand(response.getExamId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/")
    public ResponseEntity<Long> modifyExamSchedule(@RequestBody ExamDto request) {

        return ResponseEntity.ok(examService.modifyExamSchedule(request));
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExamSchedule(@PathVariable("examId") Long examId) {
        examService.deleteExamSchedule(examId);

        return ResponseEntity.noContent().build();
    }
}
