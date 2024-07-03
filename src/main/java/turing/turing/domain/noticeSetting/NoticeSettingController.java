package turing.turing.domain.noticeSetting;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.noticeSetting.dto.NoticeSettingDto;

import java.util.List;

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

    @Operation(summary = "설정 조회")
    @GetMapping("")
    public ResponseEntity<List<NoticeSettingDto.ResponseDto>> readSetting(){
        //추후 spring context에서 받아옴
        Long memberId = 0L;
        String memberRole = null;
        return ResponseEntity.ok(noticeSettingService.readSetting(memberId, memberRole));
    }
}
