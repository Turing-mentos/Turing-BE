package turing.turing.domain.report.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@Builder
public class ReportReqDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class CreateDto {
        private Long studyRoomId;

        private String name;

        private String subject;

        private String comment;

        private String attitude;

        private String request;

        private boolean pay;

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UpdateDto{
        private Long reportId;
        private int paragraphNum;

        private String content;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PayDto{
        int wage;
    }
}
