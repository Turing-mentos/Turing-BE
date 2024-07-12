package turing.turing.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

public class NoticeDto {
    @Builder
    @Getter
    @Setter
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
    public static class RemindNoteBookDto{
        private Long studentId;
    }


    @Builder
    @Getter
    @Setter
    public static class ResponseForNotice{
        private Long receiverId;
        private final String receiverRole = "STUDENT";
        private Long senderId;
        private final String senderRole = "TEACHER";
    }
}
