package turing.turing.domain.noticeSetting.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class NoticeSettingDto {
    @Builder
    @Getter
    public static class ResponseDto{
        Long noticeSettingId;
        boolean enabled;

        String category;


    }
}
