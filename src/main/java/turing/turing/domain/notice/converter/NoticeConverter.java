package turing.turing.domain.notice.converter;

import lombok.AllArgsConstructor;
import turing.turing.domain.notice.Notice;
import turing.turing.domain.notice.NoticeRepository;
import turing.turing.domain.notice.dto.NoticeDto;
import turing.turing.domain.noticeSetting.NoticeSetting;
import turing.turing.domain.noticeSetting.NoticeSettingRepository;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NoticeConverter {
    public static NoticeDto.ResponseDto toDto(Notice notice) {
        return NoticeDto.ResponseDto.builder()
                .id(notice.getId())
                .body(notice.getBody())
                .title(notice.getTitle())
                .targetId(notice.getTargetId())
                .readStatus(notice.getReadStatus())
                .category(notice.getCategory())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    public static List<NoticeDto.ResponseDto> toDtoList(List<Notice> entityList) {
        return entityList.stream()
                .map(NoticeConverter::toDto)
                .collect(Collectors.toList());
    }
}
