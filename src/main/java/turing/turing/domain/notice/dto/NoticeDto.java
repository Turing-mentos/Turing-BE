package turing.turing.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

public class NoticeDto {
    @Builder
    @Getter
    public static class ResponseDto{
        private Long id;
        private boolean readStatus;

        private String title;

        private String body;

        private String category;

        private Timestamp createdAt;

        private Long targetId;
    }
}
