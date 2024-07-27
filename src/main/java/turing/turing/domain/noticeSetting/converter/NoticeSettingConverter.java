package turing.turing.domain.noticeSetting.converter;

import turing.turing.domain.noticeSetting.NoticeSetting;
import turing.turing.domain.noticeSetting.dto.response.NoticeSettingResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class NoticeSettingConverter {
    public static List<NoticeSettingResponseDto.ResponseDto> toDtoList(List<NoticeSetting> noticeSettingList) {
        return noticeSettingList.stream()
                .map(NoticeSettingConverter::toDto)
                .collect(Collectors.toList());
    }

    public static NoticeSettingResponseDto.ResponseDto toDto(NoticeSetting noticeSetting){
        return NoticeSettingResponseDto.ResponseDto
                .builder()
                .noticeSettingId(noticeSetting.getId())
                .category(noticeSetting.getCategory())
                .enabled(noticeSetting.getEnabled())
                .build();
    }
}
