package turing.turing.domain.noticeSetting.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NoticeSettingResponseDto {
    @Builder
    @Getter
    public static class ResponseDto{
        Long noticeSettingId;
        boolean enabled;

        String category;


    }
}
