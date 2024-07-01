package turing.turing.domain.notice;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.notice.NoticeService;

@RequestMapping("/api/notification/")
@RestController
@AllArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "알림 읽음 처리")
    @PatchMapping("{notificationId}")
    public void checkNotification(@PathVariable(name = "notificationId") Long notificationId){
        noticeService.checkNotification(notificationId);
    }

    @Operation(summary = "알림 개수 조회")
    @GetMapping("")
    public void unCheckedNotification() {
        noticeService.unCheckedNotification();
    }
}
mo