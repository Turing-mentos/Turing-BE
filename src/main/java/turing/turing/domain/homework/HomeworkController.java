package turing.turing.domain.homework;

import java.net.URI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import turing.turing.domain.homework.dto.CreateHomeworkRequest;
import turing.turing.domain.homework.dto.DetailedHomeworkDto;
import turing.turing.domain.homework.dto.UpdateHomeworkRequest;

@Tag(name = "Homework", description = "숙제")
@RestController
@RequestMapping("/api/homework")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;

    @Operation(summary = "숙제 상세 조회")
    @GetMapping("/{homeworkId}")
    public ResponseEntity<DetailedHomeworkDto> getHomework(@PathVariable Long homeworkId) {

        return ResponseEntity.ok(homeworkService.getHomework(homeworkId));
    }

    @Operation(summary = "숙제 생성")
    @PostMapping("")
    public ResponseEntity<Long> createHomework(@RequestBody CreateHomeworkRequest request) {
        Long savedId = homeworkService.createHomework(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{homeworkId}")
                .buildAndExpand(savedId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "숙제 삭제")
    @DeleteMapping("/{homeworkId}")
    public ResponseEntity<Void> deleteHomework(@PathVariable Long homeworkId) {
        homeworkService.deleteHomework(homeworkId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "숙제 수정")
    @PutMapping("")
    public ResponseEntity<Long> updateHomework(@RequestBody UpdateHomeworkRequest request) {
        return ResponseEntity.ok(homeworkService.updateHomework(request));
    }

    @Operation(summary = "숙제 완료")
    @PatchMapping("")
    public ResponseEntity<Long> updateDone(@RequestBody Long homeworkId) {

        return ResponseEntity.ok(homeworkService.updateDone(homeworkId));
    }
}
