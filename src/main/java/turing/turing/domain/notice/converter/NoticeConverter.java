package turing.turing.domain.notice.converter;

import lombok.AllArgsConstructor;
import turing.turing.domain.notice.Notice;
import turing.turing.domain.notice.dto.response.NoticeResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NoticeConverter {
    public static NoticeResponseDto.ResponseDto toDto(Notice notice) {
        return NoticeResponseDto.ResponseDto.builder()
                .id(notice.getId())
                .body(notice.getBody())
                .title(notice.getTitle())
                .targetId(notice.getTargetId())
                .readStatus(notice.getReadStatus())
                .category(notice.getCategory())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    public static List<NoticeResponseDto.ResponseDto> toDtoList(List<Notice> entityList) {
        return entityList.stream()
                .map(NoticeConverter::toDto)
                .collect(Collectors.toList());
    }
}
