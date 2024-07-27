package turing.turing.domain.notebook;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.member.Role;
import turing.turing.domain.notebook.dto.CheckLatestRequest;
import turing.turing.domain.notebook.dto.CheckLatestResponse;
import turing.turing.domain.notebook.dto.CreateNotebookRequest;
import turing.turing.domain.notebook.dto.CreateNotebookResponse;
import turing.turing.domain.notebook.dto.HomeworkPercentAllDto;
import turing.turing.domain.notebook.dto.ModifyDeadlineDto;
import turing.turing.domain.notebook.dto.NotebookInfo;

@Tag(name = "Notebook", description = "알림장")
@RestController
@RequestMapping("/api/notebook")
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;

    @Operation(summary = "기존 알림장 조회(간편/현재)")
    @GetMapping("/{notebookId}")
    public ResponseEntity<NotebookInfo> getNotebook(@PathVariable("notebookId") Long notebookId, @RequestParam("new") Boolean b) {
        NotebookInfo notebookInfo = notebookService.getNotebook(notebookId, b);

        return ResponseEntity.ok(notebookInfo);
    }

    @Operation(summary = "지난 알림장 전체 조회")
    @GetMapping("/past")
    public ResponseEntity<List<NotebookInfo>> getPastNotebooks(@RequestParam("studyRoomId") Long studyRoomId, @RequestParam(value = "notebookId", required = false) Long notebookId) {

        return ResponseEntity.ok(notebookService.getPastNotebooks(studyRoomId, notebookId));
    }

    @Operation(summary = "이번주 숙제 현황 조회")
    @GetMapping("")
    public ResponseEntity<List<NotebookInfo>> getThisWeekNotebook(@RequestParam("studyRoomIds") List<Long> studyRoomIds) {

        return ResponseEntity.ok(notebookService.getThisWeekNotebook(studyRoomIds));
    }

    @Operation(summary = "알림장 생성")
    @PostMapping("")
    public ResponseEntity<CreateNotebookResponse> createNotebook(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreateNotebookRequest request) {
        CreateNotebookResponse response = notebookService.createNotebooks(customUserDetails, request);
        //수정
        response.setSender(customUserDetails.getMemberId(), Role.TEACHER);


        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{notebookId}")
                .buildAndExpand(response.getNotebookId())
                .toUri();

        return ResponseEntity.created(location).body(response);

    }

    @Operation(summary = "알림장 삭제")
    @DeleteMapping("/{notebookId}")
    public ResponseEntity<Void> deleteNotebook(@PathVariable Long notebookId) {
        notebookService.deleteNotebook(notebookId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "알림장 마감일자 수정")
    @PatchMapping("")
    public ResponseEntity<Long> modifyDeadline(@RequestBody ModifyDeadlineDto request) {


        return ResponseEntity.ok(notebookService.modifyDeadline(request));
    }

    @Operation(summary = "가장 최근 일정에 대해 알림장이 존재하는지 여부 확인")
    @GetMapping("/latest-notebook")
    public ResponseEntity<CheckLatestResponse> checkLatestNotebook(@RequestParam("studyRoomId") Long studyRoomId) {

        return ResponseEntity.ok(notebookService.isExistLatestNotebook(studyRoomId));
    }

    @Operation(summary = "숙제 온도 조회")
    @GetMapping("/completion-percent")
    public ResponseEntity<List<HomeworkPercentAllDto>> readPercent(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(notebookService.readPercent(userDetails));
    }
}
