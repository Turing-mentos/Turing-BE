package turing.turing.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ReportResDto {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class ReadDto {
        private Long reportId;

        private String opening;

        private String studyProgress;

        private String feedback;

        private String money;
        private String closing;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class CreateDto {
        private Long reportId;

    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class ReadListDto {
        private Long reportId;

        private String name;

        private String subject;

        private Integer session;
    }
}
