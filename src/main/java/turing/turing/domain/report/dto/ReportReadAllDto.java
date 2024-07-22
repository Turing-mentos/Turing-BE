package turing.turing.domain.report.dto;

import java.time.LocalDateTime;


public interface ReportReadAllDto {
    Long getReportId();

    Long getStudentId();
    String getName();

    String getSubject();

    int getSession();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
