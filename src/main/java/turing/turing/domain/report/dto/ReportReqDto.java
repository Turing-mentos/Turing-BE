package turing.turing.domain.report.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class ReportReqDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
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
    @NoArgsConstructor
    @Builder
    public static class UpdateDto{
        private Long reportId;
        private int paragraphNum;

        private String content;
    }

    public static class PayDto{
        
    }
}
