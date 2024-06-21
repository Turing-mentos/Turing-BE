package turing.turing.domain.homework;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import turing.turing.domain.homework.dto.DetailedHomeworkDto;

@RestController
@RequestMapping("/api/homework")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;

    @GetMapping("/{homeworkId}")
    public ResponseEntity<DetailedHomeworkDto> getHomework(@PathVariable Long homeworkId) {

        return ResponseEntity.ok(homeworkService.getHomework(homeworkId));
    }

    @PostMapping("")
    public ResponseEntity<Long> createHomework(@RequestBody DetailedHomeworkDto request) {
        Long savedId = homeworkService.createHomework(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{homeworkId}")
                .buildAndExpand(savedId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{homeworkId}")
    public ResponseEntity<Long> deleteHomework(@PathVariable Long homeworkId) {
        homeworkService.deleteHomework(homeworkId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("")
    public ResponseEntity<Long> updateHomework(@RequestBody DetailedHomeworkDto request) {
        return ResponseEntity.ok(homeworkService.updateHomework(request));
    }

    @PatchMapping("")
    public ResponseEntity<Long> updateDone(@RequestBody Long homeworkId) {

        return ResponseEntity.ok(homeworkService.updateDone(homeworkId));
    }
}
