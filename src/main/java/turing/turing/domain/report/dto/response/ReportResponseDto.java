package turing.turing.domain.report.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ReportResponseDto {
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
        private String firstName;
        private String lastName;
        private int currentSession;
        private int totalSession;
    }
}
