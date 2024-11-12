package turing.turing.domain.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
public class NoticeDto {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ResponseDto{
        private Long id;
        private boolean readStatus;

        private String title;

        private String body;

        private String category;

        private Timestamp createdAt;

        private Long targetId;
    }

    @SuperBuilder
    @Getter
    public static class ResponseForNotice extends BaseNoticeInfo{

    }
}
