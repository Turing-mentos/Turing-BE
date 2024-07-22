package turing.turing.domain.noticeSetting;

import org.springframework.http.ResponseEntity;
import turing.turing.domain.noticeSetting.dto.NoticeSettingDto;

import java.util.List;
import java.util.stream.Collectors;

public class NoticeSettingConverter {
    public static List<NoticeSettingDto.ResponseDto> toDtoList(List<NoticeSetting> noticeSettingList) {
        return noticeSettingList.stream()
                .map(NoticeSettingConverter::toDto)
                .collect(Collectors.toList());
    }

    public static NoticeSettingDto.ResponseDto toDto(NoticeSetting noticeSetting){
        return NoticeSettingDto.ResponseDto
                .builder()
                .noticeSettingId(noticeSetting.getId())
                .category(noticeSetting.getCategory())
                .enabled(noticeSetting.getEnabled())
                .build();
    }
}
