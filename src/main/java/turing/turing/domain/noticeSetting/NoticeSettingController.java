package turing.turing.domain.noticeSetting;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notification/setting/")
public class NoticeSettingController {
    private final NoticeSettingService noticeSettingService;

    @Operation(summary = "설정 변경")
    @PatchMapping("{notificationSettingId}")
    public void checkNotification(@PathVariable(name = "notificationSettingId") Long notificationSettingId){
        noticeSettingService.changeSetting(notificationSettingId);
    }
}
