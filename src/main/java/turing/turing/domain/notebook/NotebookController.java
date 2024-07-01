package turing.turing.domain.notebook;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import turing.turing.domain.notebook.dto.CreateNotebookDto;
import turing.turing.domain.notebook.dto.ModifyDeadlineDto;
import turing.turing.domain.notebook.dto.NotebookInfo;

@RestController
@RequestMapping("/api/notebook")
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;

    @GetMapping("/{notebookId}")
    public ResponseEntity<NotebookInfo> getNotebook(@PathVariable("notebookId") Long notebookId, @RequestParam("new") Boolean b) {
        NotebookInfo notebookInfo = notebookService.getNotebook(notebookId, b);

        return ResponseEntity.ok(notebookInfo);
    }

    @GetMapping("/past")
    public ResponseEntity<List<NotebookInfo>> getPastNotebooks(@RequestParam("studyRoomId") Long studyRoomId, @RequestParam(value = "notebookId", required = false) Long notebookId) {

        return ResponseEntity.ok(notebookService.getPastNotebooks(studyRoomId, notebookId));
    }

    @GetMapping("")
    public ResponseEntity<List<NotebookInfo>> getThisWeekNotebook(@RequestParam("studyRoomIds") List<Long> studyRoomIds) {

        return ResponseEntity.ok(notebookService.getThisWeekNotebook(studyRoomIds));
    }

    @PostMapping("")
    public ResponseEntity<Long> createNotebook(@RequestBody CreateNotebookDto request) {
        Long savedId = notebookService.createNotebook(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{notebookId}")
                .buildAndExpand(savedId)
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @DeleteMapping("/{notebookId}")
    public ResponseEntity<Void> deleteNotebook(@PathVariable Long notebookId) {
        notebookService.deleteNotebook(notebookId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("")
    public ResponseEntity<Long> modifyDeadline(@RequestBody ModifyDeadlineDto request) {


        return ResponseEntity.ok(notebookService.modifyDeadline(request));
    }
}
