package turing.turing.domain.noticeSetting.dto;

import lombok.Builder;

public class NoticeSettingDto {
    @Builder
    public static class ResponseDto{
        Long noticeSettingId;
        boolean enabled;

        String category;


    }
}
