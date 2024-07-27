package turing.turing.domain.noticeSetting;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.noticeSetting.dto.response.NoticeSettingResponseDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notification/setting")
public class NoticeSettingController {
    private final NoticeSettingService noticeSettingService;

    @Operation(summary = "설정 변경")
    @PatchMapping("/{notificationSettingId}")
    public ResponseEntity<Boolean> checkNotification(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "notificationSettingId") Long notificationSettingId){

        return ResponseEntity.ok(noticeSettingService.changeSetting(userDetails, notificationSettingId));
    }

    @Operation(summary = "설정 조회")
    @GetMapping("")
    public ResponseEntity<List<NoticeSettingResponseDto.ResponseDto>> readSetting(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(noticeSettingService.readSetting(userDetails));

    }


    @Operation(summary = "설정 생성")
    @GetMapping("/init")
    public ResponseEntity<List<NoticeSettingResponseDto.ResponseDto>> createSetting(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "true") boolean enabled) {

        noticeSettingService.createSetting(userDetails, enabled);

        return ResponseEntity.ok(null);
    }

}
