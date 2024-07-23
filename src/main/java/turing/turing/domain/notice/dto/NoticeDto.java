package turing.turing.domain.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ResponseForNotice{
        private Long receiverId;
        private final String receiverRole = "STUDENT";
        private Long senderId;
        private final String senderRole = "TEACHER";
    }
}
