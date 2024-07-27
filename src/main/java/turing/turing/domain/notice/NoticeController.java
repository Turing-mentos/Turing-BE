package turing.turing.domain.notice;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.notice.dto.response.NoticeResponseDto;
import turing.turing.domain.fcm.FcmServiceImpl;

import java.util.List;

@RequestMapping("/api/notification/")
@RestController
@AllArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final FcmServiceImpl fcmService;

    @Operation(summary = "알림 읽음 처리")
    @PatchMapping("{notificationId}")
    public void checkNotification(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "notificationId") Long notificationId){
        noticeService.checkNotification(userDetails, notificationId);
    }



    @Operation(summary = "읽지 않은 알림 개수 조회")
    @GetMapping("/total")
    public int unCheckedNotification(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return noticeService.unCheckedNotification(userDetails);
    }


    @Operation(summary = "알림 전체 조회")
    @GetMapping("/all")
    public ResponseEntity<List<NoticeResponseDto.ResponseDto>> readAllNotification(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(noticeService.readAllNotification(userDetails));
    }

    @Operation(summary = "리마인드 콕찌르기")
    @GetMapping("/notebook/{notebookId}")
    public ResponseEntity<String> remind(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "notebookId") Long notebookId){
        noticeService.remindNoteBook(userDetails, notebookId);
        return ResponseEntity.ok(null);
    }
}
