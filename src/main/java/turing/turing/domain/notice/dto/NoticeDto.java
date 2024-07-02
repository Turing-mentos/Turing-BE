package turing.turing.domain.notice.dto;

import lombok.Builder;

import java.sql.Timestamp;

public class NoticeDto {
    @Builder
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
