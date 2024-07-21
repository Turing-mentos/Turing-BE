package turing.turing.domain.report.dto;

import lombok.*;
import org.springframework.stereotype.Service;
import turing.turing.domain.report.Report;
import turing.turing.domain.schedule.Schedule;
import turing.turing.domain.teacher.Teacher;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
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
    public static class StudentInfoDto {
        private Long studentId;
        private String subject;
        private String name;
        private int currentSession;
        private int totalSession;
    }
}
