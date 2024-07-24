package turing.turing.domain.notice;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.notice.NoticeService;
import turing.turing.domain.notice.dto.NoticeDto;
import turing.turing.domain.notice.fcm.FcmService;
import turing.turing.domain.notice.fcm.FcmServiceImpl;
import turing.turing.domain.notice.fcm.dto.FcmSendDto;
import turing.turing.domain.notice.fcm.dto.TestDto;
import turing.turing.domain.teacher.Teacher;

import java.util.List;

@RequestMapping("/api/notification/")
@RestController
@AllArgsConstructor
@Slf4j
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
    public ResponseEntity<List<NoticeDto.ResponseDto>> readAllNotification(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(noticeService.readAllNotification(userDetails));
    }
    
//    @Operation(summary = "알림 테스트용")
//    @GetMapping("/tests")
//    public TestDto test(){
//        return fcmService.methodName7();
//    }

    @Operation(summary = "리마인드 콕찌르기")
    @GetMapping("/notebook/{studentId}")
    public ResponseEntity<String> remind(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "studentId") Long studentId){
        noticeService.remindNoteBook(userDetails, studentId);
        return ResponseEntity.ok(null);
    }
}
