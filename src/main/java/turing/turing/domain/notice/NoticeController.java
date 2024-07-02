package turing.turing.domain.notice;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.notice.NoticeService;
import turing.turing.domain.notice.dto.NoticeDto;

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


    @Operation(summary = "알림 전체 조회")
    @GetMapping("notification/all")
    public ResponseEntity<NoticeDto.ResponseDto> readAllNotification() {
        Long memberId = 0L;
        String memberRole = "TEACHER";
        return noticeService.readAllNotification(memberId, memberRole);
    }


}
