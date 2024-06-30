package turing.turing.domain.report.dto;

import lombok.Builder;

@Builder
public class ReportResDto {
    private Long reportId;

    private String opening;

    private String studyProgress;

    private String feedback;

    private String money;
    private String closing;

}
