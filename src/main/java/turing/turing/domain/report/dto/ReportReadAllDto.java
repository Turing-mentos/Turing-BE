package turing.turing.domain.report.dto;

import java.time.LocalDateTime;


public interface ReportReadAllDto {
    Long getReportId();

    String getFirstName();
    String getLastName();

    String getSubject();

    int getSession();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
